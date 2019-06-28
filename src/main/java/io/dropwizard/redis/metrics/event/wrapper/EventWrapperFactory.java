package io.dropwizard.redis.metrics.event.wrapper;

import io.lettuce.core.cluster.event.ClusterTopologyChangedEvent;
import io.lettuce.core.event.Event;
import io.lettuce.core.event.connection.ConnectedEvent;
import io.lettuce.core.event.connection.ConnectionActivatedEvent;
import io.lettuce.core.event.connection.ConnectionDeactivatedEvent;
import io.lettuce.core.event.connection.DisconnectedEvent;
import io.lettuce.core.event.metrics.CommandLatencyEvent;

import java.util.Optional;

public class EventWrapperFactory {
    public static VisitableEventWrapper build(final CommandLatencyEvent event) {
        return new VisitableCommandLatencyEventWrapper(event);
    }

    public static VisitableEventWrapper build(final ClusterTopologyChangedEvent event) {
        return new VisitableClusterTopologyEventWrapper(event);
    }

    public static VisitableEventWrapper build(final ConnectedEvent event) {
        return new VisitableConnectedEventWrapper(event);
    }

    public static VisitableEventWrapper build(final ConnectionActivatedEvent event) {
        return new VisitableConnectionActivatedEventWrapper(event);
    }

    public static VisitableEventWrapper build(final ConnectionDeactivatedEvent event) {
        return new VisitableConnectionDeactivatedEventWrapper(event);
    }

    public static VisitableEventWrapper build(final DisconnectedEvent event) {
        return new VisitableDisconnectedEventWrapper(event);
    }

    public static Optional<VisitableEventWrapper> build(final Event event) {
        // TODO: any way to avoid needing to do these instanceof checks?
        if (event instanceof CommandLatencyEvent) {
            return Optional.of(build((CommandLatencyEvent) event));
        } else if (event instanceof ClusterTopologyChangedEvent) {
            return Optional.of(build((ClusterTopologyChangedEvent) event));
        } else if (event instanceof ConnectedEvent) {
            return Optional.of(build((ConnectedEvent) event));
        } else if (event instanceof ConnectionActivatedEvent) {
            return Optional.of(build((ConnectionActivatedEvent) event));
        } else if (event instanceof ConnectionDeactivatedEvent) {
            return Optional.of(build((ConnectionDeactivatedEvent) event));
        } else if (event instanceof DisconnectedEvent) {
            return Optional.of(build((DisconnectedEvent) event));
        }

        return Optional.empty();
    }
}
