package app;

import app.parser.interpreters.variables.SessionHandler;
import app.responses.ErrorResponse;
import app.responses.TokenResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MatrixScript implements RequestStreamHandler {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(MatrixScript.class);

    public void handleRequest(InputStream in, OutputStream out, Context context) throws IOException {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        JsonNode payload = mapper.readTree(in);
        String path = payload.get("rawPath").textValue();

        switch (path) {
            case "/health":
                mapper.writeValue(out, "OK");
                break;
            case "/token":
                token(out);
                break;
        }
    }


    private void token(OutputStream out) throws IOException {
        String sessionToken = UUID.randomUUID().toString();
        int success = SessionHandler.createSession(sessionToken);
        if (success == -1) {
            logger.error("500 ERROR - token generation error on server side");
            mapper.writeValue(out, new ErrorResponse(500, "error generating token"));
        }

        mapper.writeValue(out, new TokenResponse(null, sessionToken));
    }
}
