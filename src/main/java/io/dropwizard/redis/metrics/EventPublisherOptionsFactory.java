package io.dropwizard.redis.metrics;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;
import io.lettuce.core.event.EventPublisherOptions;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface EventPublisherOptionsFactory extends Discoverable {
    EventPublisherOptions build();
}
