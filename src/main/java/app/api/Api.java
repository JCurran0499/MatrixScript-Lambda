package app.api;

import app.api.routes.CommandRoute;
import app.api.routes.HealthRoute;
import app.api.routes.TokenRoute;
import app.api.routes.priv.DeleteSessionRoute;
import app.api.routes.priv.ListSessionsRoute;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Api {
    private static final Map<String, Route> routes = Stream.of(new Object[][]{
        {"GET /health", HealthRoute.of()},
        {"POST /token", TokenRoute.of()},
        {"POST /", CommandRoute.of()},
        {"GET /private/list-sessions", ListSessionsRoute.of()},
        {"DELETE /private/token/{token}", DeleteSessionRoute.of()}
    }).collect(Collectors.toMap(r -> (String)r[0], r -> (Route)r[1]));

    public static Response run(String path, Payload payload) {
        return routes.get(path).execute(payload);
    }
}
