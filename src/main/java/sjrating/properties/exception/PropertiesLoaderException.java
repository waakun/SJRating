package sjrating.properties.exception;

public class PropertiesLoaderException extends Exception {

    public PropertiesLoaderException(String message, Throwable cause) {
        super(message + "\n" + cause.getMessage(), cause);
    }

}
