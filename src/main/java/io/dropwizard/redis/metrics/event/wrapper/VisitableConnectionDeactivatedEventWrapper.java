package io.dropwizard.redis.metrics.event.wrapper;

import io.dropwizard.redis.metrics.event.visitor.EventVisitor;
import io.lettuce.core.event.connection.ConnectionDeactivatedEvent;

import static java.util.Objects.requireNonNull;

public class VisitableConnectionDeactivatedEventWrapper implements VisitableEventWrapper {
    private final ConnectionDeactivatedEvent event;

    public VisitableConnectionDeactivatedEventWrapper(final ConnectionDeactivatedEvent event) {
        this.event = requireNonNull(event);
    }

    @Override
    public void accept(final EventVisitor visitor) {
        visitor.visit(event);
    }
}
