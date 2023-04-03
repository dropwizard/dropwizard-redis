package io.dropwizard.redis;

import brave.Tracing;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.redis.clientoptions.ClientOptionsFactory;
import io.dropwizard.redis.health.RedisHealthCheck;
import io.dropwizard.redis.managed.RedisClientManager;
import io.dropwizard.redis.metrics.event.LettuceMetricsSubscriber;
import io.dropwizard.redis.uri.RedisURIFactory;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.resource.ClientResources;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@JsonTypeName("basic")
public class RedisClientFactory<K, V> extends AbstractRedisClientFactory<K, V> {
    @Valid
    @NotNull
    @JsonProperty
    private RedisURIFactory node;

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
        final RedisURI uri = node.build();

        final ClientResources resources = clientResources.build(name, metrics, tracing);

        final RedisClient redisClient = RedisClient.create(resources, uri);

        redisClient.setOptions(clientOptions.build());

        final RedisCodec<K, V> codec = redisCodec.build();

        final StatefulRedisConnection<K, V> connection = redisClient.connect(codec);

        // manage client and connection
        lifecycle.manage(new RedisClientManager<K, V>(redisClient, connection, name));

        // health check
        healthChecks.register(name, new RedisHealthCheck(() -> connection.sync().ping()));

        // metrics (latency and other connection events) integration
        redisClient.getResources()
                .eventBus()
                .get()
                .subscribe(new LettuceMetricsSubscriber(buildEventVisitors(metrics)));

        return connection;
    }
}
