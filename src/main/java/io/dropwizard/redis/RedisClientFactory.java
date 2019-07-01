package io.dropwizard.redis;

import brave.Tracing;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.redis.clientoptions.ClientOptionsFactory;
import io.lettuce.core.api.StatefulRedisConnection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonTypeName("basic")
public class RedisClientFactory<K, V> extends AbstractRedisClientFactory<K, V> {
    @Valid
    @NotNull
    @JsonProperty
    private ClientOptionsFactory clientOptions = new ClientOptionsFactory();

    @Override
    public StatefulRedisConnection<K, V> build(final HealthCheckRegistry healthChecks, final LifecycleEnvironment lifecycle,
                                               final MetricRegistry metrics) {
        return build(healthChecks, lifecycle, metrics, null);
    }

    @Override
    public StatefulRedisConnection<K, V> build(final HealthCheckRegistry healthChecks, final LifecycleEnvironment lifecycle,
                                               final MetricRegistry metrics, final Tracing tracing) {
        // TODO: implement me
        throw new IllegalStateException("Not yet implemented");
    }
}
