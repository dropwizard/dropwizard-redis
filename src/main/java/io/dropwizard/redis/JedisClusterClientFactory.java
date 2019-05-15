package io.dropwizard.redis;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.redis.health.RedisHealthCheck;
import io.dropwizard.redis.managed.RedisClientManager;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.commands.JedisClusterCommands;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonTypeName("jedis")
public class JedisClusterClientFactory extends RedisClusterClientFactory {
    @Valid
    @NotNull
    @JsonProperty
    private Set<Node> nodes;

    // TODO: Security is not yet supported for the Jedis Cluster client
//    @Valid
//    @JsonProperty
//    private SecurityFactory security;

    public Set<Node> getNodes() {
        return nodes;
    }

    public void setNodes(final Set<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public JedisClusterCommands build(final MetricRegistry metrics, final HealthCheckRegistry healthChecks,
                                      final LifecycleEnvironment lifecycle) throws Exception {
        final JedisPoolConfig jedisPoolConfig = pool.build();
        JedisCluster jedisCluster;

        final Set<HostAndPort> hostAndPorts = nodes.stream()
                .map(node -> new HostAndPort(node.getHost(), node.getPort()))
                .collect(Collectors.toSet());

        // TODO: Security is not yet supported for the Jedis Cluster client
//        if (security != null && metricsEnabled) {
//            return new JedisCluster(hostAndPorts, (int) connectionTimeout.toMilliseconds(), (int) socketTimeout.toMicroseconds(), maxAttempts,
//                    password, name, security.isSslEnabled(), security.getSslSocketFactory(), security.getSslParameters(),
//                    security.getHostnameVerifier(), jedisPoolConfig);
//            jedisCluster = new InstrumentedTracingJedisCluster(hostAndPorts, (int) connectionTimeout.toMilliseconds(),
//                    (int) socketTimeout.toMicroseconds(), maxAttempts, password, name, jedisPoolConfig, , metrics, name);
//        } else if (security != null) {
//            jedisCluster = new JedisCluster(hostAndPorts, (int) connectionTimeout.toMilliseconds(),
//                    (int) socketTimeout.toMicroseconds(), maxAttempts, password, name, jedisPoolConfig);
        if (metricsEnabled) {
            jedisCluster = new InstrumentedJedisCluster(hostAndPorts, (int) connectionTimeout.toMilliseconds(),
                    (int) socketTimeout.toMicroseconds(), maxAttempts, password, name, jedisPoolConfig, metrics, name);
        } else {
            jedisCluster = new JedisCluster(hostAndPorts, (int) connectionTimeout.toMilliseconds(),
                    (int) socketTimeout.toMicroseconds(), maxAttempts, password, name, jedisPoolConfig);
        }

        // Set up health check
        healthChecks.register(name, createRedisHealthCheck(() -> jedisCluster.echo(RedisHealthCheck.HEALTHY_STRING)));

        // Set up manager
        lifecycle.manage(new RedisClientManager(jedisCluster, name));

        // TODO: set up brave tracing

        return jedisCluster;
    }
}
