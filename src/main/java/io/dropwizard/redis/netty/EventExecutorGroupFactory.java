package io.dropwizard.redis.netty;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.netty.util.concurrent.EventExecutorGroup;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface EventExecutorGroupFactory {
    EventExecutorGroup build(final int threadPoolSize, final String name, final MetricRegistry metrics);
}
