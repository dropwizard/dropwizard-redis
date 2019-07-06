package io.dropwizard.redis.managed;

import io.dropwizard.lifecycle.Managed;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.api.StatefulConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

/**
 * Manages the Redis client lifecycle alongside the application's lifecycle.
 */
public class RedisClientManager<K, V> implements Managed {
    private static final Logger log = LoggerFactory.getLogger(RedisClientManager.class);

    private final AbstractRedisClient client;
    private final StatefulConnection<K, V> connection;
    private final String name;

    public RedisClientManager(final AbstractRedisClient client, final StatefulConnection<K, V> connection, final String name) {
        this.client = requireNonNull(client);
        this.connection = requireNonNull(connection);
        this.name = requireNonNull(name);
    }

    @Override
    public void start() throws Exception {
        log.info("redis={} starting", name);
    }

    @Override
    public void stop() throws Exception {
        connection.close();
        client.shutdown();
        log.info("redis={} shut down", name);
    }
}
