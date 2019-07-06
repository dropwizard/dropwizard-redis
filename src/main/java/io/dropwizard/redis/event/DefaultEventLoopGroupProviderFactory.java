package io.dropwizard.redis.event;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.lettuce.core.resource.DefaultEventLoopGroupProvider;
import io.lettuce.core.resource.EventLoopGroupProvider;

@JsonTypeName("default")
public class DefaultEventLoopGroupProviderFactory implements EventLoopGroupProviderFactory {
    @Override
    public EventLoopGroupProvider build(int threads) {
        return new DefaultEventLoopGroupProvider(threads);
    }
}
