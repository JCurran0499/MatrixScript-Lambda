package app.responses;


import org.jetbrains.annotations.Nullable;

public record CommandResponse(
    String status,
    @Nullable String response,
    @Nullable String matrix,
    @Nullable String errMessage) {

}
