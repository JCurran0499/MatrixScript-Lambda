package app.api.routes.priv;

import app.api.Payload;
import app.api.Response;
import app.api.Route;
import app.api.responses.ListSessionsResponse;
import lombok.NonNull;
import resources.aws.AwsService;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ListSessionsRoute implements Route {

    private ListSessionsRoute() {}
    private static final ListSessionsRoute route = new ListSessionsRoute();

    public static ListSessionsRoute of() {
        return route;
    }

    public Response execute(Payload req) {
        List<Map<String, String>> mapList = AwsService.getAllItems();
        List<Session> sessionList = mapList.stream().map(
            item -> new Session(item.get("SessionToken"), Integer.parseInt(item.get("TTL")))
        ).toList();

        ListSessionsResponse.SessionJson[] sessionData = new ListSessionsResponse.SessionJson[sessionList.size()];

        for (int i = 0; i < sessionList.size(); i++) {
            Instant ttl = Instant.ofEpochSecond(sessionList.get(i).expiration());

            sessionData[i] = new ListSessionsResponse.SessionJson(
                sessionList.get(i).sessionToken(),
                ttl.atZone(ZoneId.of("-05:00")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'EST'"))
            );
        }

        return new ListSessionsResponse(sessionList.size(), sessionData).resp();
    }

    public record Session (
        @NonNull String sessionToken,
        @NonNull long expiration
    ) {}
}
