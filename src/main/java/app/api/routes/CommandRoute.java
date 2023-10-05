package app.api.routes;

import app.api.Payload;
import app.api.Response;
import app.api.Route;
import app.api.responses.CommandResponse;
import app.api.responses.ErrorResponse;
import app.parser.Parser;
import app.parser.interpreters.Primitive;
import app.parser.interpreters.variables.SessionHandler;

public class CommandRoute implements Route {

    private CommandRoute() {}
    private static final CommandRoute route = new CommandRoute();

    public static CommandRoute of() {
        return route;
    }

    public Response execute(Payload req) {
        String sessionToken = req.params().get("token").textValue();
        if (sessionToken == null)
            return new ErrorResponse("no session token").resp(400);

        if (!SessionHandler.validSession(sessionToken)) {
            return new ErrorResponse("invalid session token").resp(401);
        }

        String command = req.body().get("command").textValue();

        if (command.contains("//"))
            command = command.substring(0, command.indexOf("//")).stripTrailing();

        Primitive result = Parser.parse(sessionToken, command).solve();
        return new CommandResponse("success", result.string(), null, null).resp();
    }
}
