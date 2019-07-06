package io.dropwizard.redis.uri;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;
import io.dropwizard.util.Duration;
import io.lettuce.core.RedisURI;

import javax.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class RedisURIFactory implements Discoverable {
    @NotNull
    @JsonProperty
    protected Duration timeout = Duration.seconds(RedisURI.DEFAULT_TIMEOUT);

    @JsonProperty
    protected String clientName;

    @JsonProperty
    protected String password;

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

    public abstract RedisURI build();
}
