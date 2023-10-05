package app.api;

import com.fasterxml.jackson.databind.JsonNode;

public record Payload(
    JsonNode params,
    JsonNode path,
    JsonNode body
) {
    public static String retrieveText(JsonNode json, String field) {
        if (json == null || json.get(field) == null)
            return null;

        return json.get(field).textValue();
    }
}
