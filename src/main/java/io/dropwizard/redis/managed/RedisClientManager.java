package io.dropwizard.redis.managed;

import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;

import static java.util.Objects.requireNonNull;

/**
 * Manages {@link Closeable} client with the application lifecycle.
 */
public class RedisClientManager implements Managed {
    private static final Logger log = LoggerFactory.getLogger(RedisClientManager.class);

    private final Closeable client;
    private final String name;

    public RedisClientManager(final Closeable client, final String name) {
        this.client = requireNonNull(client);
        this.name = requireNonNull(name);
    }

    @Override
    public void start() throws Exception {
        log.info("redis={} starting", name);
    }

    @Override
    public void stop() throws Exception {
        log.info("redis={} shutting down", name);
        client.close();
    }
}
