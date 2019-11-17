package io.dropwizard.redis.metrics.event.wrapper;

import io.dropwizard.redis.metrics.event.visitor.EventVisitor;
import io.lettuce.core.event.connection.DisconnectedEvent;

import static java.util.Objects.requireNonNull;

public class VisitableDisconnectedEventWrapper implements VisitableEventWrapper {
    private final DisconnectedEvent event;

    public VisitableDisconnectedEventWrapper(final DisconnectedEvent event) {
        this.event = requireNonNull(event);
    }

    @Override
    public void accept(final EventVisitor visitor) {
        visitor.visit(event);
    }
}
