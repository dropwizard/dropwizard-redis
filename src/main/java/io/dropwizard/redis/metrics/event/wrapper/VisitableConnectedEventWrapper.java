package io.dropwizard.redis.metrics.event.wrapper;

import io.dropwizard.redis.metrics.event.visitor.ConnectedEventVisitor;
import io.lettuce.core.event.connection.ConnectedEvent;

import static java.util.Objects.requireNonNull;

public class VisitableConnectedEventWrapper implements VisitableEventWrapper<ConnectedEventVisitor> {
    private final ConnectedEvent event;

    public VisitableConnectedEventWrapper(final ConnectedEvent event) {
        this.event = requireNonNull(event);
    }

    @Override
    public void accept(final ConnectedEventVisitor visitor) {
        visitor.visit(event);
    }
}
