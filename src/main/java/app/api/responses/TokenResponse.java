package app.api.responses;


import app.api.Response;
import lombok.*;


@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class TokenResponse {
    private String status;
    private @NonNull String sessionToken;

    public Response resp(int status) {
        return new Response(status, this);
    }
}
