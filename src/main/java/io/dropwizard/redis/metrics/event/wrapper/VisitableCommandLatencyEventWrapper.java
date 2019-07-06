package io.dropwizard.redis.metrics.event.wrapper;

import io.dropwizard.redis.metrics.event.visitor.CommandLatencyEventVisitor;
import io.lettuce.core.event.metrics.CommandLatencyEvent;

import static java.util.Objects.requireNonNull;

public class VisitableCommandLatencyEventWrapper implements VisitableEventWrapper<CommandLatencyEventVisitor> {
    private final CommandLatencyEvent event;

    public VisitableCommandLatencyEventWrapper(final CommandLatencyEvent event) {
        this.event = requireNonNull(event);
    }

    @Override
    public void accept(final CommandLatencyEventVisitor visitor) {
        visitor.visit(event);
    }
}
