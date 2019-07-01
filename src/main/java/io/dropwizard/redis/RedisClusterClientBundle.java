package io.dropwizard.redis;

import brave.Tracing;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;

import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

public abstract class RedisClusterClientBundle<K, V, T extends Configuration> implements ConfiguredBundle<T> {
    @Nullable
    private StatefulRedisClusterConnection<K, V> clusterConnection;

    @Override
    public void initialize(final Bootstrap<?> bootstrap) {
        // do nothing
    }

    @Override
    public void run(final T configuration, final Environment environment) throws Exception {
        final RedisClusterClientFactory<K, V> redisClusterClientFactory = requireNonNull(getRedisClusterClientFactory(configuration));

        final Tracing tracing = Tracing.current();

        this.clusterConnection = redisClusterClientFactory.build(environment.healthChecks(), environment.lifecycle(), environment.metrics(),
                tracing);
    }

    public abstract RedisClusterClientFactory<K, V> getRedisClusterClientFactory(T configuration);

    public StatefulRedisClusterConnection<K, V> getClusterConnection() {
        return requireNonNull(clusterConnection);
    }
}
