package io.dropwizard.redis.topology;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.util.Duration;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;

import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ClusterTopologyRefreshOptionsFactory {
    @JsonProperty
    private boolean periodicRefreshEnabled = ClusterTopologyRefreshOptions.DEFAULT_PERIODIC_REFRESH_ENABLED;

    @NotNull
    @JsonProperty
    private Duration refreshPeriod = Duration.seconds(ClusterTopologyRefreshOptions.DEFAULT_REFRESH_PERIOD);

    @JsonProperty
    private boolean closeStaleConnections = ClusterTopologyRefreshOptions.DEFAULT_CLOSE_STALE_CONNECTIONS;

    @JsonProperty
    private boolean dynamicRefreshSources = ClusterTopologyRefreshOptions.DEFAULT_DYNAMIC_REFRESH_SOURCES;

    @NotNull
    @JsonProperty
    private Set<ClusterTopologyRefreshOptions.RefreshTrigger> adaptiveRefreshTriggers =
            ClusterTopologyRefreshOptions.DEFAULT_ADAPTIVE_REFRESH_TRIGGERS;

    @Min(0)
    @JsonProperty
    private int refreshTriggersReconnectAttempts = ClusterTopologyRefreshOptions.DEFAULT_REFRESH_TRIGGERS_RECONNECT_ATTEMPTS;

    public ClusterTopologyRefreshOptions build() {
        return ClusterTopologyRefreshOptions.builder()
                .enablePeriodicRefresh(periodicRefreshEnabled)
                .refreshPeriod(java.time.Duration.ofSeconds(refreshPeriod.toSeconds()))
                .closeStaleConnections(closeStaleConnections)
                .dynamicRefreshSources(dynamicRefreshSources)
                .enableAdaptiveRefreshTrigger(adaptiveRefreshTriggers.toArray(new ClusterTopologyRefreshOptions.RefreshTrigger[0]))
                .build();
    }
}
