package io.dropwizard.redis.event;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.lettuce.core.event.DefaultEventBus;
import io.lettuce.core.event.EventBus;
import reactor.core.scheduler.Scheduler;

@JsonTypeName("default")
public class DefaultEventBusFactory implements EventBusFactory {
    @Override
    public EventBus build(final Scheduler scheduler) {
        return new DefaultEventBus(scheduler);
    }
}
