package io.dropwizard.redis.clientoptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.redis.socket.SocketOptionsFactory;
import io.dropwizard.redis.ssl.SslOptionsFactory;
import io.dropwizard.redis.timeout.TimeoutOptionsFactory;
import io.lettuce.core.ClientOptions;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ClientOptionsFactory {
    @JsonProperty
    protected boolean pingBeforeActivateConnection = ClientOptions.DEFAULT_PING_BEFORE_ACTIVATE_CONNECTION;

    @JsonProperty
    protected boolean cancelCommandsOnReconnectFailure = ClientOptions.DEFAULT_CANCEL_CMD_RECONNECT_FAIL;

    @JsonProperty
    protected boolean publishOnScheduler = ClientOptions.DEFAULT_PUBLISH_ON_SCHEDULER;

    @JsonProperty
    protected boolean autoReconnect = ClientOptions.DEFAULT_AUTO_RECONNECT;

    @JsonProperty
    protected boolean suspendReconnectOnProtocolFailure = ClientOptions.DEFAULT_SUSPEND_RECONNECT_PROTO_FAIL;

    @Min(0)
    @JsonProperty
    protected int requestQueueSize = ClientOptions.DEFAULT_REQUEST_QUEUE_SIZE;

    @NotNull
    @JsonProperty
    protected ClientOptions.DisconnectedBehavior disconnectedBehavior = ClientOptions.DEFAULT_DISCONNECTED_BEHAVIOR;

    @Valid
    @NotNull
    @JsonProperty
    protected SocketOptionsFactory socketOptions = new SocketOptionsFactory();

    @Valid
    @JsonProperty
    protected SslOptionsFactory sslOptions;

    @Valid
    @NotNull
    @JsonProperty
    protected TimeoutOptionsFactory timeoutOptions = new TimeoutOptionsFactory();

    public boolean isPingBeforeActivateConnection() {
        return pingBeforeActivateConnection;
    }

    public void setPingBeforeActivateConnection(final boolean pingBeforeActivateConnection) {
        this.pingBeforeActivateConnection = pingBeforeActivateConnection;
    }

    public boolean isCancelCommandsOnReconnectFailure() {
        return cancelCommandsOnReconnectFailure;
    }

    public void setCancelCommandsOnReconnectFailure(final boolean cancelCommandsOnReconnectFailure) {
        this.cancelCommandsOnReconnectFailure = cancelCommandsOnReconnectFailure;
    }

    public boolean isPublishOnScheduler() {
        return publishOnScheduler;
    }

    public void setPublishOnScheduler(final boolean publishOnScheduler) {
        this.publishOnScheduler = publishOnScheduler;
    }

    public boolean isAutoReconnect() {
        return autoReconnect;
    }

    public void setAutoReconnect(final boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }

    public boolean isSuspendReconnectOnProtocolFailure() {
        return suspendReconnectOnProtocolFailure;
    }

    public void setSuspendReconnectOnProtocolFailure(final boolean suspendReconnectOnProtocolFailure) {
        this.suspendReconnectOnProtocolFailure = suspendReconnectOnProtocolFailure;
    }

    public int getRequestQueueSize() {
        return requestQueueSize;
    }

    public void setRequestQueueSize(final int requestQueueSize) {
        this.requestQueueSize = requestQueueSize;
    }

    public ClientOptions.DisconnectedBehavior getDisconnectedBehavior() {
        return disconnectedBehavior;
    }

    public void setDisconnectedBehavior(final ClientOptions.DisconnectedBehavior disconnectedBehavior) {
        this.disconnectedBehavior = disconnectedBehavior;
    }

    public SocketOptionsFactory getSocketOptions() {
        return socketOptions;
    }

    public void setSocketOptions(final SocketOptionsFactory socketOptions) {
        this.socketOptions = socketOptions;
    }

    public SslOptionsFactory getSslOptions() {
        return sslOptions;
    }

    public void setSslOptions(final SslOptionsFactory sslOptions) {
        this.sslOptions = sslOptions;
    }

    public TimeoutOptionsFactory getTimeoutOptions() {
        return timeoutOptions;
    }

    public void setTimeoutOptions(final TimeoutOptionsFactory timeoutOptions) {
        this.timeoutOptions = timeoutOptions;
    }

    public ClientOptions build() {
        final ClientOptions.Builder builder = ClientOptions.builder();

        return addBuildParams(builder).build();
    }

    protected ClientOptions.Builder addBuildParams(final ClientOptions.Builder builder) {
         builder.pingBeforeActivateConnection(pingBeforeActivateConnection)
                .cancelCommandsOnReconnectFailure(cancelCommandsOnReconnectFailure)
                .publishOnScheduler(publishOnScheduler)
                .autoReconnect(autoReconnect)
                .suspendReconnectOnProtocolFailure(suspendReconnectOnProtocolFailure)
                .requestQueueSize(requestQueueSize)
                .disconnectedBehavior(disconnectedBehavior)
                .socketOptions(socketOptions.build())
                .timeoutOptions(timeoutOptions.build());

        if (sslOptions != null && sslOptions.isEnabled()) {
            builder.sslOptions(sslOptions.build());
        }

        return builder;
    }
}
