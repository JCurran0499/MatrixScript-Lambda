package app.api.routes.priv;

import app.api.Payload;
import app.api.Response;
import app.api.Route;
import app.api.responses.ListSessionsResponse;
import app.parser.interpreters.variables.SessionHandler;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListSessionsRoute implements Route {

    private ListSessionsRoute() {}
    private static final ListSessionsRoute route = new ListSessionsRoute();

    public static ListSessionsRoute of() {
        return route;
    }

    public Response execute(Payload req) {
        List<SessionHandler.Session> sessionList = SessionHandler.allSessions();
        ListSessionsResponse.SessionJson[] sessionData = new ListSessionsResponse.SessionJson[sessionList.size()];

        for (int i = 0; i < sessionList.size(); i++) {
            Instant ttl = Instant.ofEpochSecond(sessionList.get(i).getExpiration());

            sessionData[i] = new ListSessionsResponse.SessionJson(
                sessionList.get(i).getSessionToken(),
                ttl.atZone(ZoneId.of("-05:00")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'EST'"))
            );
        }

        return new ListSessionsResponse(sessionList.size(), sessionData).resp();
    }
}
