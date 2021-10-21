package io.dropwizard.redis.uri;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.net.HostAndPort;
import io.lettuce.core.RedisURI;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Set;

@JsonTypeName("sentinel")
public class SentinelModeURIFactory extends RedisURIFactory {
    @Valid
    @NotEmpty
    @JsonProperty
    private Set<HostAndPort> sentinels = Collections.emptySet();

    @JsonProperty
    private String sentinelMasterId;

    public Set<HostAndPort> getSentinels() {
        return sentinels;
    }

    public void setSentinels(final Set<HostAndPort> sentinels) {
        this.sentinels = sentinels;
    }

    public String getSentinelMasterId() {
        return sentinelMasterId;
    }

    public void setSentinelMasterId(final String sentinelMasterId) {
        this.sentinelMasterId = sentinelMasterId;
    }

    @Override
    public RedisURI build() {
        final RedisURI.Builder builder = RedisURI.builder()
                .withTimeout(java.time.Duration.ofSeconds(timeout.toSeconds()));

        sentinels.forEach(sentinel -> builder.withSentinel(sentinel.getHost(), sentinel.getPort()));

        if (clientName != null) {
            builder.withClientName(clientName);
        }

        if (username != null && password != null) {
            builder.withAuthentication(username, password);
        } else if (password != null) {
            builder.withPassword(password);
        }

        if (sentinelMasterId != null) {
            builder.withSentinelMasterId(sentinelMasterId);
        }

        return builder.build();
    }
}
