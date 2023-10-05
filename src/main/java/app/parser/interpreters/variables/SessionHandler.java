package app.parser.interpreters.variables;

import app.parser.interpreters.Primitive;

import java.util.*;

import app.MatrixScript;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resources.aws.AwsService;

public class SessionHandler {

    private static final Logger logger = LoggerFactory.getLogger(MatrixScript.class);


    /* ---------- Session Methods ---------- */

    public static int createSession(String sessionToken) {
        AwsService.addItem(sessionToken);

        return 0;
    }

    public static int invalidateSession(String sessionToken) {
        AwsService.deleteItem(sessionToken);

        return 0;
    }

    public static boolean validSession(String sessionToken) {
        return AwsService.itemExists(sessionToken);
    }

    public static List<Session> allSessions() {
        List<Map<String, String>> mapList = AwsService.getAllItems();
        return mapList.stream().map(item ->
            new Session(item.get("SessionToken"), Integer.valueOf(item.get("TTL")))
        ).toList();
    }


    /* ---------- Variable Methods ---------- */
    public static Primitive getVar(String sessionToken, String var) {
        return AwsService.getAttribute(sessionToken, var);
    }

    public static void setVar(String sessionToken, String varName, Primitive val) {
        AwsService.putAttribute(sessionToken, varName, val);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Session {
        @NonNull String sessionToken;
        @NonNull long expiration;
    }
}
