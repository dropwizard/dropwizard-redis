package io.dropwizard.redis.metrics.event.wrapper;

import io.dropwizard.redis.metrics.event.visitor.EventVisitor;
import io.lettuce.core.event.metrics.CommandLatencyEvent;

import static java.util.Objects.requireNonNull;

public class VisitableCommandLatencyEventWrapper implements VisitableEventWrapper {
    private final CommandLatencyEvent event;

    public VisitableCommandLatencyEventWrapper(final CommandLatencyEvent event) {
        this.event = requireNonNull(event);
    }

    @Override
    public void accept(final EventVisitor visitor) {
        visitor.visit(event);
    }
}
