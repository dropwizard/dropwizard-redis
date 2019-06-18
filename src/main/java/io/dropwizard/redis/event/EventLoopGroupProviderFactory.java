package io.dropwizard.redis.event;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;
import io.lettuce.core.resource.EventLoopGroupProvider;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface EventLoopGroupProviderFactory extends Discoverable {
    EventLoopGroupProvider build(int threads);
}
