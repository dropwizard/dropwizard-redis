package io.dropwizard.redis.event;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;
import io.lettuce.core.event.EventBus;
import reactor.core.scheduler.Scheduler;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface EventBusFactory extends Discoverable {
    EventBus build(final Scheduler scheduler);
}
