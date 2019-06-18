package io.dropwizard.redis;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.redis.clientresources.ClientResourcesFactory;
import io.dropwizard.redis.clientresources.DefaultClientResourcesFactory;
import io.dropwizard.redis.codec.RedisCodecFactory;
import io.dropwizard.redis.uri.RedisURIFactory;
import io.dropwizard.validation.MinSize;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.tracing.Tracing;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class AbstractRedisClientFactory<K, V> implements Discoverable {
    @NotNull
    @JsonProperty
    protected String name;

    @Valid
    @NotNull
    @MinSize(1)
    @JsonProperty
    protected List<RedisURIFactory> nodes = Collections.emptyList();

    @Valid
    @JsonProperty
    protected ClusterTopologyRefreshOptions clusterTopologyRefreshOptions;

    @Valid
    @NotNull
    @JsonProperty
    protected ClientResourcesFactory clientResources = new DefaultClientResourcesFactory();

    @Valid
    @NotNull
    @JsonProperty
    protected RedisCodecFactory<K, V> redisCodec;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<RedisURIFactory> getNodes() {
        return nodes;
    }

    public void setNodes(final List<RedisURIFactory> nodes) {
        this.nodes = nodes;
    }

    public ClusterTopologyRefreshOptions getClusterTopologyRefreshOptions() {
        return clusterTopologyRefreshOptions;
    }

    public void setClusterTopologyRefreshOptions(final ClusterTopologyRefreshOptions clusterTopologyRefreshOptions) {
        this.clusterTopologyRefreshOptions = clusterTopologyRefreshOptions;
    }

    public ClientResourcesFactory getClientResources() {
        return clientResources;
    }

    public void setClientResources(final ClientResourcesFactory clientResources) {
        this.clientResources = clientResources;
    }

    public RedisCodecFactory<K, V> getRedisCodec() {
        return redisCodec;
    }

    public void setRedisCodec(final RedisCodecFactory<K, V> redisCodec) {
        this.redisCodec = redisCodec;
    }

    public StatefulConnection<K, V> build(final HealthCheckRegistry healthChecks, final LifecycleEnvironment lifecycle,
                                          final MetricRegistry metrics) {
        return build(healthChecks, lifecycle, metrics, null);
    }

    public abstract StatefulConnection<K, V> build(final HealthCheckRegistry healthChecks, final LifecycleEnvironment lifecycle,
                                                   final MetricRegistry metrics, final Tracing tracing);
}
