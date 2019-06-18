package en.ubb.networkconfiguration.business.listener;


import en.ubb.networkconfiguration.business.service.NetworkService;
import en.ubb.networkconfiguration.business.validation.exception.NetworkAccessBussExc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Observer for Lifecycle events
 */
@Slf4j
@Component
public class LifecycleResponseHandler implements ApplicationListener<LifecycleEvent> {

    private final NetworkService networkService;

    @Autowired
    public LifecycleResponseHandler(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public void onApplicationEvent(LifecycleEvent event) {
        log.info("Handing lifecycle event for network with id {0} and status {1}",
                event.getNetwork().getId(), event.getState().name());
        try {
            switch (event.getState()) {
                case STARTED:
                    break;
                case STOPPED:
                    networkService.saveProgress(event.getNetwork());
                    break;
                case FAILED:
                    break;
                case SCORE_IMPROVED:
                    log.info("Score for network with id {0} improved", event.getNetwork().getId());
                default:
                    break;

            }


        } catch (NetworkAccessBussExc ex) {
            ex.printStackTrace();
        }

    }
}
