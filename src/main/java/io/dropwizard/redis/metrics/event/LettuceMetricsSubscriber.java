package io.dropwizard.redis.metrics.event;

import io.dropwizard.redis.metrics.event.visitor.EventVisitor;
import io.dropwizard.redis.metrics.event.wrapper.EventWrapperFactory;
import io.dropwizard.redis.metrics.event.wrapper.VisitableEventWrapper;
import io.lettuce.core.event.Event;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class LettuceMetricsSubscriber implements Consumer<Event> {
    private final List<EventVisitor> eventVisitors;

    public LettuceMetricsSubscriber(List<EventVisitor> eventVisitors) {
        this.eventVisitors = eventVisitors;
    }

    @Override
    public void accept(Event event) {
        final Optional<VisitableEventWrapper> eventWrapperOpt = EventWrapperFactory.build(event);
        eventWrapperOpt.ifPresent(eventWrapper -> eventVisitors.forEach(eventWrapper::accept));
    }
}
