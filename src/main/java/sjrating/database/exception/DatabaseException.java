package sjrating.database.exception;

public class DatabaseException extends Exception {
    public DatabaseException(String message, Throwable cause) {
        super(message + "\n" + cause.getMessage(), cause);
    }
}
