package sjrating.service.exception;

public class ServiceException extends Exception {

    public ServiceException(String message, Throwable cause) {
        super(message + "\n" + cause.getMessage(), cause);
    }
}
