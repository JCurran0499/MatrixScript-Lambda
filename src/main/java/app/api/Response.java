package app.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Response {
    private static final ObjectMapper mapper = new ObjectMapper();

    private @NonNull int statusCode;
    private @NonNull Map<String, String> headers = new HashMap<>();
    private String body;

    public Response(int status, Object data) {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        statusCode = status;
        headers.put("Content-Type", "application/json");

        try {
            body = mapper.writeValueAsString(data);
        } catch (IOException e) {
            body = null;
        }
    }
}
