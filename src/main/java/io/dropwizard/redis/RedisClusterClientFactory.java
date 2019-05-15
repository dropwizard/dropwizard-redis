package io.dropwizard.redis;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.redis.health.Pingable;
import io.dropwizard.redis.health.RedisHealthCheck;
import io.dropwizard.redis.pool.JedisPoolConfigFactory;
import io.dropwizard.util.Duration;
import redis.clients.jedis.commands.JedisClusterCommands;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Factory to build {@link JedisClusterCommands} instances.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class RedisClusterClientFactory implements Discoverable {
    @NotNull
    @JsonProperty
    protected String name;

    @JsonProperty
    protected boolean metricsEnabled = true;

    @Valid
    @NotNull
    @JsonProperty
    protected JedisPoolConfigFactory pool = new JedisPoolConfigFactory();

    @NotNull
    @JsonProperty
    protected Duration connectionTimeout = Duration.seconds(2);

    @NotNull
    @JsonProperty
    protected Duration socketTimeout = Duration.seconds(2);

    @JsonProperty
    protected String password;

    @Min(-1)
    @JsonProperty
    protected int maxAttempts = 5; // matches BinaryJedisCluster.DEFAULT_MAX_ATTEMPTS

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isMetricsEnabled() {
        return metricsEnabled;
    }

    public void setMetricsEnabled(final boolean metricsEnabled) {
        this.metricsEnabled = metricsEnabled;
    }

    public JedisPoolConfigFactory getPool() {
        return pool;
    }

    public void setPool(final JedisPoolConfigFactory pool) {
        this.pool = pool;
    }

    public Duration getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(final Duration connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Duration getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(final Duration socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(final int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    /**
     * By default, creates a master-based {@link RedisHealthCheck}. May be overridden to customize health-check logic if desired.
     * @param client pinag-able client.
     * @return Health check.
     */
    protected RedisHealthCheck createRedisHealthCheck(final Pingable client) {
        return new RedisHealthCheck(client);
    }

    public abstract JedisClusterCommands build(MetricRegistry metrics, HealthCheckRegistry healthChecks, LifecycleEnvironment lifecycle)
            throws Exception;
}
