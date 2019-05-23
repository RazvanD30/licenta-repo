package en.ubb.networkconfiguration.business.listener;

import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class LifecycleEvent extends ApplicationEvent {

    private Network network;
    private NetworkLifecycleState state;

    /**
     * Create a new LifecycleEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public LifecycleEvent(Object source, Network network, NetworkLifecycleState state) {
        super(source);
        this.network = network;
        this.state = state;
    }


}
