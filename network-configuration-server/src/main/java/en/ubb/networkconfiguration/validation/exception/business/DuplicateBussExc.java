package en.ubb.networkconfiguration.validation.exception.business;

public class DuplicateBussExc extends BusinessException {

    public DuplicateBussExc() {
    }

    public DuplicateBussExc(String message) {
        super(message);
    }

    public DuplicateBussExc(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateBussExc(Throwable cause) {
        super(cause);
    }

    public DuplicateBussExc(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
