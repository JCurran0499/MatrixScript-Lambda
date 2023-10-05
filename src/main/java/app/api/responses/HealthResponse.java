package app.api.responses;

import app.api.Response;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HealthResponse {
    private @NonNull String status;

    public Response resp() {
        return new Response(200, this);
    }
}
