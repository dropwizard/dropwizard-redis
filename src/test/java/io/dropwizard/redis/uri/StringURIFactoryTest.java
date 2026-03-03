package io.dropwizard.redis.uri;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.lettuce.core.RedisURI;
import java.time.Duration;
import org.junit.jupiter.api.Test;

import java.io.File;

import jakarta.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class StringURIFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<RedisURIFactory> configFactory = new YamlConfigurationFactory<>(
            RedisURIFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildARedisURIFromString() throws Exception {
        final File yml = new File(Resources.getResource("yaml/uri/string-redis-uri.yaml").toURI());
        final RedisURIFactory factory = configFactory.build(yml);

        assertThat(factory)
                .isInstanceOf(StringURIFactory.class);

        final StringURIFactory stringFactory = (StringURIFactory) factory;

        assertThat(stringFactory.getUri())
                .isEqualTo("redis://hunter2@127.0.0.1:6379/1?timeout=90s&clientName=test");

        final RedisURI redisURI = factory.build();
        assertThat(redisURI).isInstanceOf(RedisURI.class);
        assertThat(redisURI.getHost()).isEqualTo("127.0.0.1");
        assertThat(redisURI.getPort()).isEqualTo(6379);
        assertThat(redisURI.getDatabase()).isEqualTo(1);
        assertThat(redisURI.getTimeout()).isEqualTo(Duration.ofSeconds(90));
        assertThat(redisURI.getClientName()).isEqualTo("test");
    }

    @Test
    public void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(StringURIFactory.class);
    }
}
