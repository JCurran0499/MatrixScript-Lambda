package resources.aws;

import app.parser.interpreters.Primitive;
import org.apache.commons.lang3.SerializationUtils;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.regions.Region;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AwsService {



    private static final SnsClient sns = SnsClient.builder()
        .region(Region.US_EAST_1)
        .build();

    private static final DynamoDbClient dynamodb = (!AwsConstants.ENDPOINT.isEmpty())
        ? DynamoDbClient.builder()
            .region(Region.US_EAST_1)
            .endpointOverride(URI.create(System.getenv("ENDPOINT")))
            .build()
        : DynamoDbClient.builder()
            .region(Region.US_EAST_1)
            .build();


    /* SNS */
    public static void publish(String topicArn, String command, String message) {
        sns.publish(PublishRequest.builder()
            .topicArn(topicArn)
            .message(String.format(AwsConstants.ERROR_MESSAGE, command, message))
            .subject(AwsConstants.ERROR_SUBJECT)
            .build()
        );
    }


    /* DynamoDB */
    public static int addItem(String sessionToken) {
        long ttl = Instant.now().getEpochSecond() + AwsConstants.TTL;

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("SessionToken", AttributeValue.builder().s(sessionToken).build());
        item.put("TTL", AttributeValue.builder().n(String.valueOf(ttl)).build());

        try {
            dynamodb.putItem(PutItemRequest.builder()
                .tableName(System.getenv("TABLE_NAME"))
                .item(item)
                .conditionExpression("attribute_not_exists(SessionToken)")
                .build()
            );

            return 0;
        } catch (ConditionalCheckFailedException e) {
            return -1;
        }
    }

    public static int deleteItem(String sessionToken) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("SessionToken", AttributeValue.builder().s(sessionToken).build());

        try {
            dynamodb.deleteItem(DeleteItemRequest.builder()
                .tableName(System.getenv("TABLE_NAME"))
                .key(key)
                .conditionExpression("attribute_exists(SessionToken)")
                .build()
            );

            return 0;
        } catch (ConditionalCheckFailedException e) {
            return -1;
        }
    }

    public static boolean itemExists(String sessionToken) {
        long now = Instant.now().getEpochSecond();

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("SessionToken", AttributeValue.builder().s(sessionToken).build());

        Map<String, String> k = new HashMap<>();
        k.put("#k", "TTL");

        GetItemResponse resp = dynamodb.getItem(GetItemRequest.builder()
            .tableName(System.getenv("TABLE_NAME"))
            .key(key)
            .projectionExpression("SessionToken,#k")
            .expressionAttributeNames(k)
            .build()
        );

        return resp.hasItem() && Long.parseLong(resp.item().get("TTL").n()) >= now;
    }

    public static Primitive getAttribute(String sessionToken, String attribute) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("SessionToken", AttributeValue.builder().s(sessionToken).build());

        Map<String, String> a = new HashMap<>();
        a.put("#a", attribute);

        GetItemResponse resp = dynamodb.getItem(GetItemRequest.builder()
            .tableName(System.getenv("TABLE_NAME"))
            .key(key)
            .projectionExpression("#a")
            .expressionAttributeNames(a)
            .build()
        );

        if (!resp.hasItem() || !resp.item().containsKey(attribute))
            return null;

        return SerializationUtils.deserialize(resp.item().get(attribute).b().asByteArray());
    }

    public static List<Map<String, String>> getAllItems() {
        long now = Instant.now().getEpochSecond();

        Map<String, String> k = new HashMap<>();
        k.put("#k", "TTL");

        Map<String, AttributeValue> v = new HashMap<>();
        v.put(":v", AttributeValue.builder().n(String.valueOf(now)).build());

        ScanResponse resp = dynamodb.scan(ScanRequest.builder()
            .tableName(System.getenv("TABLE_NAME"))
            .select("SPECIFIC_ATTRIBUTES")
            .projectionExpression("SessionToken,#k")
            .filterExpression("#k > :v")
            .expressionAttributeNames(k)
            .expressionAttributeValues(v)
            .build()
        );

        if (!resp.hasItems()) {
            return new ArrayList<>();
        }

        List<Map<String, String>> allItems = new ArrayList<>();
        for (Map<String, AttributeValue> item : resp.items()) {
            Map<String, String> itemMap = new HashMap<>();
            itemMap.put("SessionToken", item.get("SessionToken").s());
            itemMap.put("TTL", item.get("TTL").n());

            allItems.add(itemMap);
        }

        return allItems;
    }

    public static void putAttribute(String sessionToken, String name, Primitive val) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("SessionToken", AttributeValue.builder().s(sessionToken).build());

        Map<String, String> k = new HashMap<>();
        k.put("#k", name);

        byte[] bytes = SerializationUtils.serialize(val);
        Map<String, AttributeValue> v = new HashMap<>();
        v.put(":v", AttributeValue.builder().b(SdkBytes.fromByteArray(bytes)).build());

        dynamodb.updateItem(UpdateItemRequest.builder()
            .tableName(System.getenv("TABLE_NAME"))
            .key(key)
            .updateExpression("SET #k = :v")
            .expressionAttributeNames(k)
            .expressionAttributeValues(v)
            .build()
        );
    }
}
