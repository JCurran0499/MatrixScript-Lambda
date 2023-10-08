package app.api.routes;

import app.api.Payload;
import app.api.Response;
import app.api.Route;
import app.api.responses.CommandResponse;
import app.api.responses.ErrorResponse;
import app.parser.Parser;
import app.parser.interpreters.Primitive;
import app.parser.interpreters.primitives.Declare;
import app.parser.interpreters.primitives.Err;
import app.parser.interpreters.primitives.Mat;
import resources.aws.AwsService;

public class CommandRoute implements Route {

    private CommandRoute() {}
    private static final CommandRoute route = new CommandRoute();

    public static CommandRoute of() {
        return route;
    }

    public Response execute(Payload req) {
        String sessionToken = Payload.retrieveText(req.params(), "token");
        String command = Payload.retrieveText(req.body(), "command");

        if (sessionToken == null || command == null)
            return new ErrorResponse("invalid payload format").resp(400);

        if (!AwsService.itemExists(sessionToken))
            return new ErrorResponse("invalid session token").resp(401);


        if (command.contains("//"))
            command = command.substring(0, command.indexOf("//")).stripTrailing();

        try {
            Primitive result = Parser.parse(sessionToken, command).solve();
            return jsonValue(result).resp();
        } catch (Exception e) {
            AwsService.publish(System.getenv("SNS_TOPIC"), command, e.getLocalizedMessage());
            return new ErrorResponse("unexpected internal server error").resp(500);
        }
    }

    private CommandResponse jsonValue(Primitive result) {
        if (Mat.is(result)) {
            String matString = result.string().replaceAll("\n", "n");
            return new CommandResponse("success", null, matString, null);
        }
        else if (Err.is(result)) {
            return new CommandResponse("error", null, null, result.string());
        }
        else if (Declare.is(result)) {
            return new CommandResponse("success", "", null, null);
        }
        else {
            return new CommandResponse("success", result.string(), null, null);
        }
    }
}
