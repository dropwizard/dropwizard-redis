package io.dropwizard.redis.socket;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.util.Duration;
import io.lettuce.core.SocketOptions;

import jakarta.validation.constraints.NotNull;

public class SocketOptionsFactory {
    @NotNull
    @JsonProperty
    private Duration connectTimeout = Duration.seconds(SocketOptions.DEFAULT_CONNECT_TIMEOUT);

    @JsonProperty
    private boolean keepAlive = SocketOptions.DEFAULT_SO_KEEPALIVE;

    @JsonProperty
    private boolean tcpNoDelay = SocketOptions.DEFAULT_SO_NO_DELAY;

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(final Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(final boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public void setTcpNoDelay(final boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public SocketOptions build() {
        return SocketOptions.builder()
                .connectTimeout(java.time.Duration.ofSeconds(connectTimeout.toSeconds()))
                .keepAlive(keepAlive)
                .tcpNoDelay(tcpNoDelay)
                .build();
    }
}
