package io.dropwizard.redis.netty;

import com.codahale.metrics.InstrumentedThreadFactory;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.ThreadFactory;

@JsonTypeName("default")
public class DefaultEventExecutorGroupFactory implements EventExecutorGroupFactory {
    @Override
    public EventExecutorGroup build(final int threadPoolSize, final String name, final MetricRegistry metrics) {
        String poolName = String.format("%s-eventExecutorLoop", name);
        final ThreadFactory threadFactory = new DefaultThreadFactory(poolName, true);
        return new DefaultEventExecutorGroup(threadPoolSize, new InstrumentedThreadFactory(threadFactory, metrics, poolName));
    }
}
