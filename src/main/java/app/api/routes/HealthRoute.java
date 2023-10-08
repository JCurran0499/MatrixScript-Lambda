package app.api.routes;

import app.api.Payload;
import app.api.Response;
import app.api.Route;
import app.api.responses.CommandResponse;

public class HealthRoute implements Route {

    private HealthRoute() {}
    private static final HealthRoute route = new HealthRoute();

    public static HealthRoute of() {
        return route;
    }

    public Response execute(Payload req) {
        return new CommandResponse("OK").resp();
    }
}
