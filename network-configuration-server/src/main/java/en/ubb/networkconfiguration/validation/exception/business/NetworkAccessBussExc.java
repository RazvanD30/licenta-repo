package en.ubb.networkconfiguration.validation.exception.business;

public class NetworkAccessBussExc extends BusinessException {

    public NetworkAccessBussExc(String details) {
        super("Network access error. " + details);
    }
}
