package app.api;

import com.fasterxml.jackson.databind.JsonNode;

public record Payload(
    JsonNode headers,
    JsonNode params,
    JsonNode path,
    JsonNode body
) {
}
