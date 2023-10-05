package app.api.responses;

import app.api.Response;
import lombok.*;

@Getter
@AllArgsConstructor
public class CommandResponse {
    private @NonNull String status;
    private String response;
    private String matrix;
    private String errMessage;

    public Response resp() {
        return new Response(201, this);
    }
}
