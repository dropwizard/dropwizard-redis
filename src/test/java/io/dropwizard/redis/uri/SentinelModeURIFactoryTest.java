package io.dropwizard.redis.uri;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.google.common.net.HostAndPort;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.redis.delay.ConstantDelayFactory;
import io.dropwizard.util.Duration;
import io.lettuce.core.RedisURI;
import org.junit.jupiter.api.Test;

import java.io.File;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class SentinelModeURIFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<RedisURIFactory> configFactory =
            new YamlConfigurationFactory<>(RedisURIFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildASentinelRedisURI() throws Exception {
        final File yml = new File(Resources.getResource("yaml/uri/sentinel-redis-uri.yaml").toURI());
        final RedisURIFactory factory = configFactory.build(yml);

        assertThat(factory)
                .isInstanceOf(SentinelModeURIFactory.class);

        final SentinelModeURIFactory sentinelModeURIFactory = (SentinelModeURIFactory) factory;

        assertThat(sentinelModeURIFactory.getTimeout())
                .isEqualTo(Duration.seconds(90));
        assertThat(sentinelModeURIFactory.getClientName())
                .isEqualTo("test");
        assertThat(sentinelModeURIFactory.getPassword())
                .isEqualTo("hunter2");
        assertThat(sentinelModeURIFactory.getSentinels())
                .contains(HostAndPort.fromParts("127.0.0.1", 6389));
        assertThat(sentinelModeURIFactory.getSentinelMasterId())
                .isEqualTo("abc");

        assertThat(factory.build())
                .isInstanceOf(RedisURI.class);
    }

    @Test
    public void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(SentinelModeURIFactory.class);
    }
}
