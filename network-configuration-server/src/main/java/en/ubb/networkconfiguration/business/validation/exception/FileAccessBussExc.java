package en.ubb.networkconfiguration.business.validation.exception;

public class FileAccessBussExc extends BusinessException {

    public FileAccessBussExc() {
    }

    public FileAccessBussExc(String message) {
        super(message);
    }

    public FileAccessBussExc(String message, Throwable cause) {
        super(message, cause);
    }

    public FileAccessBussExc(Throwable cause) {
        super(cause);
    }

    public FileAccessBussExc(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
