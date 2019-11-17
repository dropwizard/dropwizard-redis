package io.dropwizard.redis.metrics.event.wrapper;

import io.dropwizard.redis.metrics.event.visitor.EventVisitor;
import io.lettuce.core.event.connection.ConnectedEvent;

import static java.util.Objects.requireNonNull;

public class VisitableConnectedEventWrapper implements VisitableEventWrapper {
    private final ConnectedEvent event;

    public VisitableConnectedEventWrapper(final ConnectedEvent event) {
        this.event = requireNonNull(event);
    }

    @Override
    public void accept(final EventVisitor visitor) {
        visitor.visit(event);
    }
}
