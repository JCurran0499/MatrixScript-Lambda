package resources.aws;

public class AwsConstants {
    public static final String ERROR_MESSAGE = """
        An error has occurred while a user was using MatrixScript.

        The command causing the error was "%s".
        Error message:
        %s""";

    public static final String ERROR_SUBJECT = "[ALERT] MatrixScript Error";


    private static final int MINUTE = 60;
    private static final int HOUR = 60 * MINUTE;

    public static final int TTL = 24 * HOUR;

    public static final String ENDPOINT = System.getenv("ENDPOINT");
}
