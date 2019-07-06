package io.dropwizard.redis.metrics;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;
import io.lettuce.core.metrics.CommandLatencyCollector;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface CommandLatencyCollectorFactory extends Discoverable {
    CommandLatencyCollector build();
}
