package io.dropwizard.redis.uri;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.net.HostAndPort;
import io.lettuce.core.RedisURI;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonTypeName("redis")
public class RedisModeURIFactory extends RedisURIFactory {
    @Valid
    @NotNull
    @JsonProperty
    private HostAndPort node;

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

    @Override
    public RedisURI build() {
        final RedisURI.Builder builder = RedisURI.builder()
                .withHost(node.getHost())
                .withPort(node.getPort())
                .withSsl(ssl)
                .withStartTls(startTls)
                .withVerifyPeer(verifyPeer)
                .withTimeout(java.time.Duration.ofSeconds(timeout.toSeconds()));

        if (clientName != null) {
            builder.withClientName(clientName);
        }
        
        if(username != null && password != null) {
        	builder.withAuthentication(username, password);
        } else if (password != null) {
            builder.withPassword(password);
        }
        
        return builder.build();
    }
}
