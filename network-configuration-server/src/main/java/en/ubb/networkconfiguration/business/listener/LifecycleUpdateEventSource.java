package en.ubb.networkconfiguration.business.listener;

import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@Component
public class LifecycleUpdateEventSource implements BiConsumer<Network, NetworkLifecycleState>, ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Override
    public void accept(Network network, NetworkLifecycleState lifecycleState) {
        log.info("Lifecycle status for network with id: " + network.getId() + " updated to " + lifecycleState.name());
        this.publisher.publishEvent(new LifecycleEvent(this, network, lifecycleState));
    }

}
