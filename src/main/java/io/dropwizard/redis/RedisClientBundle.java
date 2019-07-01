package io.dropwizard.redis;

import brave.Tracing;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.lettuce.core.api.StatefulRedisConnection;

import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

public abstract class RedisClientBundle <K, V, T extends Configuration> implements ConfiguredBundle<T> {
    @Nullable
    private StatefulRedisConnection<K, V> connection;

    @Override
    public void initialize(final Bootstrap<?> bootstrap) {
        // do nothing
    }

    @Override
    public void run(final T configuration, final Environment environment) throws Exception {
        final RedisClientFactory<K, V> redisClientFactory = requireNonNull(getRedisClientFactory(configuration));

        final Tracing tracing = Tracing.current();

        this.connection = redisClientFactory.build(environment.healthChecks(), environment.lifecycle(), environment.metrics(), tracing);
    }

    public abstract RedisClientFactory<K, V> getRedisClientFactory(T configuration);

    public StatefulRedisConnection<K, V> getConnection() {
        return requireNonNull(connection);
    }
}
