package en.ubb.networkconfiguration.business.validation.exception;

public class NotFoundBussExc extends BusinessException {

    public NotFoundBussExc() {
    }

    public NotFoundBussExc(String message) {
        super(message);
    }

    public NotFoundBussExc(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundBussExc(Throwable cause) {
        super(cause);
    }

    public NotFoundBussExc(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
