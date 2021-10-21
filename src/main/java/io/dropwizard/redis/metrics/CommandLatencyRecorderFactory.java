package io.dropwizard.redis.metrics;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;
import io.lettuce.core.metrics.CommandLatencyRecorder;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface CommandLatencyRecorderFactory extends Discoverable {
    CommandLatencyRecorder build(MetricRegistry metricRegistry);
}
