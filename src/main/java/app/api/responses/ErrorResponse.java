package app.api.responses;

import app.api.Response;
import lombok.*;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private @NonNull String message;

    public Response resp(int status) {
        return new Response(status, this);
    }
}
