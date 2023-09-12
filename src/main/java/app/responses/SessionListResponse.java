package app.responses;

import app.parser.interpreters.variables.SessionHandler;
import lombok.Getter;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class SessionListResponse {
    private final int sessionCount;
    private final SessionJson[] sessions;

    public SessionListResponse(int sessionCount, List<String> sessionList) {
        this.sessionCount = sessionCount;
        this.sessions = new SessionJson[sessionCount];

        for (int i = 0; i < sessionCount; i++) {
            String s = sessionList.get(i);
            this.sessions[i] = new SessionJson(
                s, SessionHandler.getExpiration(s).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"))
            );
        }
    }

    public record SessionJson(
        String token,
        String expiration) {
    }
}
