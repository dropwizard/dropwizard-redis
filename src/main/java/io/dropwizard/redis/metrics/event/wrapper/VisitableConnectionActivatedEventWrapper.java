package io.dropwizard.redis.metrics.event.wrapper;

import io.dropwizard.redis.metrics.event.visitor.ConnectionActivatedEventVisitor;
import io.lettuce.core.event.connection.ConnectionActivatedEvent;

import static java.util.Objects.requireNonNull;

public class VisitableConnectionActivatedEventWrapper implements VisitableEventWrapper<ConnectionActivatedEventVisitor> {
    private final ConnectionActivatedEvent event;

    public VisitableConnectionActivatedEventWrapper(final ConnectionActivatedEvent event) {
        this.event = requireNonNull(event);
    }

    @Override
    public void accept(final ConnectionActivatedEventVisitor visitor) {
        visitor.visit(event);
    }
}
