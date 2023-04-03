package io.dropwizard.redis;

import brave.Tracing;
import io.dropwizard.core.Configuration;
import io.dropwizard.core.ConfiguredBundle;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.lettuce.core.api.StatefulRedisConnection;
import org.checkerframework.checker.nullness.qual.Nullable;

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
