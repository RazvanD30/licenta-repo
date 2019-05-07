package en.ubb.networkconfiguration.validation.exception.business;

public class NetworkAccessBussExc extends BusinessException {

    public NetworkAccessBussExc() {
    }

    public NetworkAccessBussExc(String message) {
        super(message);
    }

    public NetworkAccessBussExc(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkAccessBussExc(Throwable cause) {
        super(cause);
    }

    public NetworkAccessBussExc(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
