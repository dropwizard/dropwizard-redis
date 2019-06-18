package io.dropwizard.redis;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.redis.clientoptions.ClusterClientOptionsFactory;
import io.dropwizard.redis.health.RedisHealthCheck;
import io.dropwizard.redis.managed.RedisClientManager;
import io.dropwizard.redis.uri.RedisURIFactory;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.tracing.Tracing;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonTypeName("cluster")
public class RedisClusterClientFactory<K, V> extends AbstractRedisClientFactory<K, V> {
    @Valid
    @NotNull
    @JsonProperty
    private ClusterClientOptionsFactory clientOptions = new ClusterClientOptionsFactory();

    public ClusterClientOptionsFactory getClientOptions() {
        return clientOptions;
    }

    public void setClientOptions(final ClusterClientOptionsFactory clientOptions) {
        this.clientOptions = clientOptions;
    }

    @Override
    public StatefulRedisClusterConnection<K, V> build(final HealthCheckRegistry healthChecks, final LifecycleEnvironment lifecycle,
                                                      final MetricRegistry metrics) {
        return build(healthChecks, lifecycle, metrics, null);
    }

    @Override
    public StatefulRedisClusterConnection<K, V> build(final HealthCheckRegistry healthChecks, final LifecycleEnvironment lifecycle,
                                                      final MetricRegistry metrics, final Tracing tracing) {

        final List<RedisURI> uris = nodes.stream()
                .map(RedisURIFactory::build)
                .collect(Collectors.toList());

        final ClientResources resources = clientResources.build(name, metrics, tracing);

        final RedisClusterClient redisClusterClient = RedisClusterClient.create(resources, uris);

        redisClusterClient.setOptions(clientOptions.build());

        final RedisCodec<K, V> codec = redisCodec.build();

        final StatefulRedisClusterConnection<K, V> connection = redisClusterClient.connect(codec);

        // manage
        lifecycle.manage(new RedisClientManager<K, V>(redisClusterClient, connection, name));

        // health checks
        healthChecks.register(name, new RedisHealthCheck(() -> connection.sync().ping()));

        return connection;
    }
}
