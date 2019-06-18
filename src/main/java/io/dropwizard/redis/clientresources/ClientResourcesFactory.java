package io.dropwizard.redis.clientresources;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;
import io.dropwizard.redis.delay.DelayFactory;
import io.dropwizard.redis.delay.ExponentialDelayFactory;
import io.dropwizard.redis.event.DefaultEventBusFactory;
import io.dropwizard.redis.event.DefaultEventLoopGroupProviderFactory;
import io.dropwizard.redis.event.EventBusFactory;
import io.dropwizard.redis.event.EventLoopGroupProviderFactory;
import io.dropwizard.redis.metrics.CommandLatencyCollectorFactory;
import io.dropwizard.redis.metrics.DefaultCommandLatencyCollectorFactory;
import io.dropwizard.redis.metrics.DefaultEventPublisherOptionsFactory;
import io.dropwizard.redis.metrics.EventPublisherOptionsFactory;
import io.dropwizard.redis.netty.DefaultEventExecutorGroupFactory;
import io.dropwizard.redis.netty.EventExecutorGroupFactory;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import io.lettuce.core.tracing.Tracing;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class ClientResourcesFactory implements Discoverable {
    @Valid
    @NotNull
    @JsonProperty
    protected CommandLatencyCollectorFactory commandLatencyCollector = new DefaultCommandLatencyCollectorFactory();

    @Valid
    @NotNull
    @JsonProperty
    protected EventPublisherOptionsFactory eventPublisherOptions = new DefaultEventPublisherOptionsFactory();

    @Valid
    @NotNull
    @JsonProperty
    protected EventBusFactory eventBusFactory = new DefaultEventBusFactory();

    @Valid
    @NotNull
    @JsonProperty
    protected EventExecutorGroupFactory eventExecutorGroup = new DefaultEventExecutorGroupFactory();

    @Valid
    @NotNull
    @JsonProperty
    protected EventLoopGroupProviderFactory eventLoopGroupProvider = new DefaultEventLoopGroupProviderFactory();

    @Min(1)
    @JsonProperty
    protected int computationThreads = DefaultClientResources.DEFAULT_COMPUTATION_THREADS;

    @Min(1)
    @JsonProperty
    protected int ioThreads = DefaultClientResources.DEFAULT_IO_THREADS;

    @Valid
    @NotNull
    protected DelayFactory delay = new ExponentialDelayFactory();


    public abstract ClientResources build(final String name, final MetricRegistry metrics, final Tracing tracing);
}
