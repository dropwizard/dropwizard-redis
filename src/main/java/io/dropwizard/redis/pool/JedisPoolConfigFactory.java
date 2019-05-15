package io.dropwizard.redis.pool;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.util.Duration;
import redis.clients.jedis.JedisPoolConfig;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Jedis connection pool configuration factory.
 */
public class JedisPoolConfigFactory {
    @Min(0)
    @JsonProperty
    private int maxTotalConnections = JedisPoolConfig.DEFAULT_MAX_TOTAL;
    @Min(0)
    @JsonProperty
    private int minIdleConnections = JedisPoolConfig.DEFAULT_MIN_IDLE;
    @Min(0)
    @JsonProperty
    private int maxIdleConnections = JedisPoolConfig.DEFAULT_MAX_IDLE;
    @JsonProperty
    private boolean jmxEnabled = JedisPoolConfig.DEFAULT_JMX_ENABLE;
    @JsonProperty
    private String jmxNameBase = JedisPoolConfig.DEFAULT_JMX_NAME_BASE;
    @NotNull
    @JsonProperty
    private String jmxNamePrefix = JedisPoolConfig.DEFAULT_JMX_NAME_PREFIX;
    @JsonProperty
    private boolean blockWhenExhausted = JedisPoolConfig.DEFAULT_BLOCK_WHEN_EXHAUSTED;
    @JsonProperty
    private Duration timeBetweenEvictionRuns;
    @JsonProperty
    private boolean testWhileIdle = JedisPoolConfig.DEFAULT_TEST_WHILE_IDLE;
    @JsonProperty
    private boolean testOnReturn = JedisPoolConfig.DEFAULT_TEST_ON_RETURN;
    @JsonProperty
    private boolean testOnBorrow = JedisPoolConfig.DEFAULT_TEST_ON_BORROW;
    @JsonProperty
    private boolean testOnCreate = JedisPoolConfig.DEFAULT_TEST_ON_CREATE;
    @NotNull
    @JsonProperty
    private Duration minEvictableIdleTime = Duration.milliseconds(JedisPoolConfig.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS);
    @JsonProperty
    private Duration softMinEvictableIdleTime;
    @JsonProperty
    private Duration maxWait;
    @Min(-1)
    @JsonProperty
    private int testsPerEvictionRun = JedisPoolConfig.DEFAULT_NUM_TESTS_PER_EVICTION_RUN;
    @NotNull
    @JsonProperty
    private String evictionPolicy = JedisPoolConfig.DEFAULT_EVICTION_POLICY_CLASS_NAME;
    @NotNull
    @JsonProperty
    private Duration evictorShutdownTimeout = Duration.milliseconds(JedisPoolConfig.DEFAULT_EVICTOR_SHUTDOWN_TIMEOUT_MILLIS);
    @JsonProperty
    private boolean fairness = JedisPoolConfig.DEFAULT_FAIRNESS;
    @JsonProperty
    private boolean lifo = JedisPoolConfig.DEFAULT_LIFO;

    public int getMaxTotalConnections() {
        return maxTotalConnections;
    }

    public void setMaxTotalConnections(final int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    public int getMinIdleConnections() {
        return minIdleConnections;
    }

    public void setMinIdleConnections(final int minIdleConnections) {
        this.minIdleConnections = minIdleConnections;
    }

    public int getMaxIdleConnections() {
        return maxIdleConnections;
    }

    public void setMaxIdleConnections(final int maxIdleConnections) {
        this.maxIdleConnections = maxIdleConnections;
    }

    public boolean isJmxEnabled() {
        return jmxEnabled;
    }

    public void setJmxEnabled(final boolean jmxEnabled) {
        this.jmxEnabled = jmxEnabled;
    }

    public String getJmxNameBase() {
        return jmxNameBase;
    }

    public void setJmxNameBase(final String jmxNameBase) {
        this.jmxNameBase = jmxNameBase;
    }

    public String getJmxNamePrefix() {
        return jmxNamePrefix;
    }

    public void setJmxNamePrefix(final String jmxNamePrefix) {
        this.jmxNamePrefix = jmxNamePrefix;
    }

    public boolean isBlockWhenExhausted() {
        return blockWhenExhausted;
    }

    public void setBlockWhenExhausted(final boolean blockWhenExhausted) {
        this.blockWhenExhausted = blockWhenExhausted;
    }

    public Duration getTimeBetweenEvictionRuns() {
        return timeBetweenEvictionRuns;
    }

    public void setTimeBetweenEvictionRuns(final Duration timeBetweenEvictionRuns) {
        this.timeBetweenEvictionRuns = timeBetweenEvictionRuns;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(final boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(final boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(final boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnCreate() {
        return testOnCreate;
    }

    public void setTestOnCreate(final boolean testOnCreate) {
        this.testOnCreate = testOnCreate;
    }

    public Duration getMinEvictableIdleTime() {
        return minEvictableIdleTime;
    }

    public void setMinEvictableIdleTime(final Duration minEvictableIdleTime) {
        this.minEvictableIdleTime = minEvictableIdleTime;
    }

    public Duration getSoftMinEvictableIdleTime() {
        return softMinEvictableIdleTime;
    }

    public void setSoftMinEvictableIdleTime(final Duration softMinEvictableIdleTime) {
        this.softMinEvictableIdleTime = softMinEvictableIdleTime;
    }

    public Duration getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(final Duration maxWait) {
        this.maxWait = maxWait;
    }

    public int getTestsPerEvictionRun() {
        return testsPerEvictionRun;
    }

    public void setTestsPerEvictionRun(final int testsPerEvictionRun) {
        this.testsPerEvictionRun = testsPerEvictionRun;
    }

    public String getEvictionPolicy() {
        return evictionPolicy;
    }

    public void setEvictionPolicy(final String evictionPolicy) {
        this.evictionPolicy = evictionPolicy;
    }

    public Duration getEvictorShutdownTimeout() {
        return evictorShutdownTimeout;
    }

    public void setEvictorShutdownTimeout(final Duration evictorShutdownTimeout) {
        this.evictorShutdownTimeout = evictorShutdownTimeout;
    }

    public boolean isFairness() {
        return fairness;
    }

    public void setFairness(final boolean fairness) {
        this.fairness = fairness;
    }

    public boolean isLifo() {
        return lifo;
    }

    public void setLifo(final boolean lifo) {
        this.lifo = lifo;
    }

    public JedisPoolConfig build() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMinIdle(minIdleConnections);
        poolConfig.setMaxIdle(maxIdleConnections);
        poolConfig.setMaxTotal(maxTotalConnections);
        poolConfig.setJmxEnabled(jmxEnabled);
        poolConfig.setJmxNameBase(jmxNameBase);
        poolConfig.setJmxNamePrefix(jmxNamePrefix);
        poolConfig.setBlockWhenExhausted(blockWhenExhausted);
        if (timeBetweenEvictionRuns != null) {
            poolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRuns.toMilliseconds());
        }
        poolConfig.setTestWhileIdle(testWhileIdle);
        poolConfig.setTestOnReturn(testOnReturn);
        poolConfig.setTestOnBorrow(testOnBorrow);
        poolConfig.setTestOnCreate(testOnCreate);
        poolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTime.toMilliseconds());
        poolConfig.setNumTestsPerEvictionRun(testsPerEvictionRun);
        if (softMinEvictableIdleTime != null) {
            poolConfig.setSoftMinEvictableIdleTimeMillis(softMinEvictableIdleTime.toMilliseconds());
        }
        if (maxWait != null) {
            poolConfig.setMaxWaitMillis(maxWait.toMilliseconds());
        }
        poolConfig.setEvictionPolicyClassName(evictionPolicy);
        poolConfig.setEvictorShutdownTimeoutMillis(evictorShutdownTimeout.toMilliseconds());
        poolConfig.setFairness(fairness);
        poolConfig.setLifo(lifo);
        return poolConfig;
    }
}
