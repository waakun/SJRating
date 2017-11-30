package sjrating.retriever.exception;

public class RetrieverException extends Exception {
    public RetrieverException(String message, Throwable cause) {
        super(message + "\n" + cause.getMessage(), cause);
    }
}
