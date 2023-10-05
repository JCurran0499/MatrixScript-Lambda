package app.api.routes.priv;

import app.api.Payload;
import app.api.Response;
import app.api.Route;
import app.api.responses.ErrorResponse;
import app.api.responses.TokenResponse;
import resources.aws.AwsService;

public class DeleteSessionRoute implements Route {

    private DeleteSessionRoute() {}
    private static final DeleteSessionRoute route = new DeleteSessionRoute();

    public static DeleteSessionRoute of() {
        return route;
    }

    public Response execute(Payload req) {
        String sessionToken = Payload.retrieveText(req.path(), "token");
        if (sessionToken == null)
            return new ErrorResponse("invalid payload format").resp(400);

        int success = AwsService.deleteItem(sessionToken);
        if (success == -1)
            return new ErrorResponse("invalid token").resp(404);

        return new TokenResponse("DELETED", sessionToken).resp(201);
    }
}
