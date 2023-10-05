package app.api.routes;

import app.api.Payload;
import app.api.Response;
import app.api.Route;
import app.api.responses.ErrorResponse;
import app.api.responses.TokenResponse;
import app.parser.interpreters.variables.SessionHandler;

import java.util.UUID;

public class TokenRoute implements Route {

    private TokenRoute() {}
    private static final TokenRoute route = new TokenRoute();

    public static TokenRoute of() {
        return route;
    }

    public Response execute(Payload req) {
        String sessionToken = UUID.randomUUID().toString();
        int success = SessionHandler.createSession(sessionToken);
        if (success == -1)
            return new ErrorResponse("error generating token").resp(500);

        return new TokenResponse(sessionToken).resp(201);
    }
}
