package io.dropwizard.redis.delay;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;
import io.lettuce.core.resource.Delay;

import java.util.function.Supplier;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface DelayFactory extends Discoverable {
    Supplier<Delay> build();
}
