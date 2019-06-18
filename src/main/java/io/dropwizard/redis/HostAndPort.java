package io.dropwizard.redis;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.PortRange;

import java.util.Objects;

import javax.validation.constraints.NotNull;

public class HostAndPort {
    @NotNull
    @JsonProperty
    private String host;

    @NotNull
    @PortRange
    @JsonProperty
    private Integer port = 6379;

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(final Integer port) {
        this.port = port;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HostAndPort)) {
            return false;
        }
        final HostAndPort that = (HostAndPort) o;
        return Objects.equals(host, that.host) &&
                Objects.equals(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}
