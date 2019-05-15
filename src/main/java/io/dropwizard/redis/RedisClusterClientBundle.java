package io.dropwizard.redis;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import redis.clients.jedis.commands.JedisClusterCommands;

import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

public abstract class RedisClusterClientBundle<T extends Configuration> implements ConfiguredBundle<T> {
    @Nullable
    private JedisClusterCommands clusterClient;

    @Override
    public void initialize(final Bootstrap<?> bootstrap) {
        // do nothing
    }

    @Override
    public void run(final T configuration, final Environment environment) throws Exception {
        final RedisClusterClientFactory redisClusterClientFactory = requireNonNull(getRedisClusterClientFactory(configuration));

        this.clusterClient = redisClusterClientFactory.build(environment.metrics(), environment.healthChecks(), environment.lifecycle());
    }

    public abstract RedisClusterClientFactory getRedisClusterClientFactory(T configuration);

    public JedisClusterCommands getClusterClient() {
        return requireNonNull(clusterClient);
    }
}
