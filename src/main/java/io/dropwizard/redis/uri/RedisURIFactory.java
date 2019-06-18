package io.dropwizard.redis.uri;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.redis.HostAndPort;
import io.dropwizard.util.Duration;
import io.lettuce.core.RedisURI;

import java.util.Collections;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RedisURIFactory {
    @Valid
    @NotNull
    @JsonProperty
    private HostAndPort node;

    @NotNull
    @JsonProperty
    private Duration timeout = Duration.seconds(RedisURI.DEFAULT_TIMEOUT);

    @JsonProperty
    private String clientName;

    @JsonProperty
    private String password;

    @Valid
    @NotNull
    @JsonProperty
    private Set<HostAndPort> sentinels = Collections.emptySet();

    @JsonProperty
    private String sentinelMasterId;

    @JsonProperty
    private boolean ssl = false;

    @JsonProperty
    private boolean startTls = false;

    @JsonProperty
    private boolean verifyPeer = true;

    public HostAndPort getNode() {
        return node;
    }

    public void setNode(final HostAndPort node) {
        this.node = node;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(final Duration timeout) {
        this.timeout = timeout;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

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

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(final boolean ssl) {
        this.ssl = ssl;
    }

    public boolean isStartTls() {
        return startTls;
    }

    public void setStartTls(final boolean startTls) {
        this.startTls = startTls;
    }

    public boolean isVerifyPeer() {
        return verifyPeer;
    }

    public void setVerifyPeer(final boolean verifyPeer) {
        this.verifyPeer = verifyPeer;
    }

    public RedisURI build() {
        final RedisURI.Builder builder = RedisURI.builder()
                .withHost(node.getHost())
                .withPort(node.getPort())
                .withTimeout(java.time.Duration.ofSeconds(timeout.toSeconds()))
                .withSsl(ssl)
                .withStartTls(startTls)
                .withVerifyPeer(verifyPeer);

        if (clientName != null) {
            builder.withClientName(clientName);
        }

        if (password != null) {
            builder.withPassword(password);
        }

        sentinels.forEach(sentinel -> builder.withSentinel(sentinel.getHost(), sentinel.getPort()));

        if (sentinelMasterId != null) {
            builder.withSentinelMasterId(sentinelMasterId);
        }

        return builder.build();
    }
}
