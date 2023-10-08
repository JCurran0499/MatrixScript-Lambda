package app.api.responses;

import app.api.Response;
import lombok.Getter;
import lombok.NonNull;
import lombok.AllArgsConstructor;


@Getter
@AllArgsConstructor
public class ListSessionsResponse {
    private int sessionCount;
    private @NonNull SessionJson[] sessions;

    public record SessionJson(
        @NonNull String token,
        @NonNull String expiration
    ) {}

    public Response resp() {
        return new Response(200, this);
    }
}