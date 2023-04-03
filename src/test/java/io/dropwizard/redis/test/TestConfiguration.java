package io.dropwizard.redis.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import io.dropwizard.redis.RedisClientFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class TestConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty("redis")
    private RedisClientFactory<String, String> redisClientFactory;

    public RedisClientFactory<String, String> getRedisClientFactory() {
        return redisClientFactory;
    }

    public void setRedisClientFactory(final RedisClientFactory<String, String> redisClientFactory) {
        this.redisClientFactory = redisClientFactory;
    }
}
