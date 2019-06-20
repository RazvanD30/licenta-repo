package en.ubb.networkconfiguration.business.validation.exception;

public class ForbiddenAccessBussExc extends BusinessException {

    public ForbiddenAccessBussExc() {
    }

    public ForbiddenAccessBussExc(String message) {
        super(message);
    }

    public ForbiddenAccessBussExc(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenAccessBussExc(Throwable cause) {
        super(cause);
    }

    public ForbiddenAccessBussExc(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
