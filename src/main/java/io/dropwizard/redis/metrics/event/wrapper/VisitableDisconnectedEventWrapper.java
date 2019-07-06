package io.dropwizard.redis.metrics.event.wrapper;

import io.dropwizard.redis.metrics.event.visitor.DisconnectedEventVisitor;
import io.lettuce.core.event.connection.DisconnectedEvent;

import static java.util.Objects.requireNonNull;

public class VisitableDisconnectedEventWrapper implements VisitableEventWrapper<DisconnectedEventVisitor> {
    private final DisconnectedEvent event;

    public VisitableDisconnectedEventWrapper(final DisconnectedEvent event) {
        this.event = requireNonNull(event);
    }

    @Override
    public void accept(final DisconnectedEventVisitor visitor) {
        visitor.visit(event);
    }
}
