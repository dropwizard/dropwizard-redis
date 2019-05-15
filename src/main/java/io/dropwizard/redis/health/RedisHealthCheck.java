package io.dropwizard.redis.health;

import com.codahale.metrics.health.HealthCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Performs health checks against a Redis cluster.
 * Health check pings the current master, determining whether it is healthy for reads/writes.
 */
public class RedisHealthCheck extends HealthCheck {
    private static final Logger log = LoggerFactory.getLogger(RedisHealthCheck.class);

    public static final String HEALTHY_STRING = "PONG";

    private final Pingable client;

    public RedisHealthCheck(final Pingable client) {
        this.client = client;
    }

    public Pingable getClient() {
        return client;
    }

    @Override
    protected Result check() {
        try {
            return HEALTHY_STRING.equals(client.ping()) ? Result.healthy() : Result.unhealthy("Could not ping Redis");
        } catch (final RuntimeException e) {
            log.warn("Redis health check failed due to exception", e);
            return Result.unhealthy(e);
        }
    }
}
