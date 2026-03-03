package io.dropwizard.redis.uri;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.lettuce.core.RedisURI;

import jakarta.validation.constraints.NotNull;

@JsonTypeName("uri")
public class StringURIFactory extends RedisURIFactory {
    @NotNull
    @JsonProperty
    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(final String uri) {
        this.uri = uri;
    }

    @Override
    public RedisURI build() {
        return RedisURI.create(uri);
    }
}
