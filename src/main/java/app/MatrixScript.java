package app;

import app.api.Api;
import app.api.Payload;
import app.api.Response;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MatrixScript implements RequestStreamHandler {

    private static final ObjectMapper mapper = new ObjectMapper();

    public void handleRequest(InputStream in, OutputStream out, Context context) throws IOException {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        JsonNode json = mapper.readTree(in);
        JsonNode body = json.has("body")
            ? mapper.readTree(json.get("body").asText())
            : null;

        String path = json.get("routeKey").textValue();
        Payload payload = new Payload(json.get("queryStringParameters"), json.get("pathParameters"), body);

        Response resp = Api.run(path, payload);
        mapper.writeValue(out, resp);
    }
}
