package app.api;

public interface Route {
    Response execute(Payload req);
}
