package en.ubb.networkconfiguration.business.validation.exception;

public class AuthorizationBussExc extends BusinessException {

    public AuthorizationBussExc() {
    }

    public AuthorizationBussExc(String message) {
        super(message);
    }

    public AuthorizationBussExc(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizationBussExc(Throwable cause) {
        super(cause);
    }

    public AuthorizationBussExc(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
