package io.dropwizard.redis;

import brave.Tracing;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.ImmutableList;
import io.dropwizard.jackson.Discoverable;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.redis.clientresources.ClientResourcesFactory;
import io.dropwizard.redis.clientresources.DefaultClientResourcesFactory;
import io.dropwizard.redis.codec.RedisCodecFactory;
import io.dropwizard.redis.metrics.event.visitor.ClusterTopologyChangedEventVisitor;
import io.dropwizard.redis.metrics.event.visitor.CommandLatencyEventVisitor;
import io.dropwizard.redis.metrics.event.visitor.ConnectedEventVisitor;
import io.dropwizard.redis.metrics.event.visitor.ConnectionActivatedEventVisitor;
import io.dropwizard.redis.metrics.event.visitor.ConnectionDeactivatedEventVisitor;
import io.dropwizard.redis.metrics.event.visitor.DisconnectedEventVisitor;
import io.dropwizard.redis.metrics.event.visitor.EventVisitor;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;

import java.util.List;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class AbstractRedisClientFactory<K, V> implements Discoverable {
    @NotNull
    @JsonProperty
    protected String name;

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
                                                   final MetricRegistry metrics, @Nullable final Tracing tracing);

    protected List<EventVisitor> buildEventVisitors(final MetricRegistry metrics) {
        // Extract this, and the event wrapper builders, to Dropwizard factories, if more event types are added frequently enough?
        return ImmutableList.of(
                new ClusterTopologyChangedEventVisitor(name, metrics),
                new CommandLatencyEventVisitor(name, metrics),
                new ConnectedEventVisitor(name, metrics),
                new ConnectionActivatedEventVisitor(name, metrics),
                new ConnectionDeactivatedEventVisitor(name, metrics),
                new DisconnectedEventVisitor(name, metrics)
        );
    }
}
