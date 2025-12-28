package ma.emsi.inscription_et_reinscription.exceptions;

public class DocumentInvalideException extends RuntimeException {
    
    public DocumentInvalideException(String message) {
        super(message);
    }
    
    public DocumentInvalideException(String message, Throwable cause) {
        super(message, cause);
    }
}
