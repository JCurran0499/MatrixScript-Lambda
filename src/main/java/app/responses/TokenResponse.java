package app.responses;


import org.jetbrains.annotations.Nullable;

public record TokenResponse(
    @Nullable String status,
    String sessionToken) {

}
