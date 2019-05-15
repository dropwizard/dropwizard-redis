package io.dropwizard.redis;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.PortRange;

import java.util.Objects;

import javax.validation.constraints.NotNull;

public class Node {
    @NotNull
    @JsonProperty
    private String host;

    @NotNull
    @PortRange
    @JsonProperty
    private Integer port;

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
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (!(that instanceof Node)) {
            return false;
        }
        final Node node = (Node) that;
        return Objects.equals(host, node.host) &&
                Objects.equals(port, node.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}
