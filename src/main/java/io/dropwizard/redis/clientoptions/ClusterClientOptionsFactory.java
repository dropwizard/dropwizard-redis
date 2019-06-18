package io.dropwizard.redis.clientoptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.redis.topology.ClusterTopologyRefreshOptionsFactory;
import io.lettuce.core.cluster.ClusterClientOptions;

import javax.validation.Valid;
import javax.validation.constraints.Min;

public class ClusterClientOptionsFactory extends ClientOptionsFactory {
    @JsonProperty
    private boolean validateClusterNodeMembership = ClusterClientOptions.DEFAULT_VALIDATE_CLUSTER_MEMBERSHIP;

    @Min(0)
    @JsonProperty
    private int maxRedirects = ClusterClientOptions.DEFAULT_MAX_REDIRECTS;

    @Valid
    @JsonProperty
    private ClusterTopologyRefreshOptionsFactory topologyRefreshOptions;

    public boolean isValidateClusterNodeMembership() {
        return validateClusterNodeMembership;
    }

    public void setValidateClusterNodeMembership(final boolean validateClusterNodeMembership) {
        this.validateClusterNodeMembership = validateClusterNodeMembership;
    }

    public int getMaxRedirects() {
        return maxRedirects;
    }

    public void setMaxRedirects(final int maxRedirects) {
        this.maxRedirects = maxRedirects;
    }

    public ClusterTopologyRefreshOptionsFactory getTopologyRefreshOptions() {
        return topologyRefreshOptions;
    }

    public void setTopologyRefreshOptions(final ClusterTopologyRefreshOptionsFactory topologyRefreshOptions) {
        this.topologyRefreshOptions = topologyRefreshOptions;
    }

    @Override
    public ClusterClientOptions build() {
        final ClusterClientOptions.Builder builder = ((ClusterClientOptions.Builder) super.addBuildParams(ClusterClientOptions.builder()))
                .validateClusterNodeMembership(validateClusterNodeMembership)
                .maxRedirects(maxRedirects);

        if (topologyRefreshOptions != null) {
            builder.topologyRefreshOptions(topologyRefreshOptions.build());
        }

        return builder.build();
    }
}
