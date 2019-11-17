package io.dropwizard.redis.metrics.event.wrapper;

import io.dropwizard.redis.metrics.event.visitor.EventVisitor;
import io.lettuce.core.event.connection.ConnectionActivatedEvent;

import static java.util.Objects.requireNonNull;

public class VisitableConnectionActivatedEventWrapper implements VisitableEventWrapper {
    private final ConnectionActivatedEvent event;

    public VisitableConnectionActivatedEventWrapper(final ConnectionActivatedEvent event) {
        this.event = requireNonNull(event);
    }

    @Override
    public void accept(final EventVisitor visitor) {
        visitor.visit(event);
    }
}
