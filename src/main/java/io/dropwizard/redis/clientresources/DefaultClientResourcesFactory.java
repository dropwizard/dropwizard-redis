package io.dropwizard.redis.clientresources;

import brave.Tracing;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import io.lettuce.core.tracing.BraveTracing;
import io.netty.util.concurrent.EventExecutorGroup;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Nullable;

@JsonTypeName("default")
public class DefaultClientResourcesFactory extends ClientResourcesFactory {
    @Override
    public ClientResources build(final String name, final MetricRegistry metrics, @Nullable final Tracing tracing) {
        final EventExecutorGroup executorGroup = eventExecutorGroup.build(computationThreads, name, metrics);

        final ClientResources.Builder builder = DefaultClientResources.builder()
                .commandLatencyRecorder(commandLatencyRecorder.build(metrics))
                .commandLatencyPublisherOptions(eventPublisherOptions.build())
                .eventExecutorGroup(executorGroup)
                .eventBus(eventBusFactory.build(Schedulers.fromExecutor(executorGroup)))
                .computationThreadPoolSize(computationThreads)
                .ioThreadPoolSize(ioThreads)
                .eventLoopGroupProvider(eventLoopGroupProvider.build(computationThreads))
                .reconnectDelay(delay.build());

        if (tracing != null) {
            builder.tracing(BraveTracing.create(tracing));
        }

        // TODO: add netty customizer
        // TODO: add dns resolver
        // TODO: add socket address resolver
        // TODO: add netty Timer

        return builder.build();
    }
}
