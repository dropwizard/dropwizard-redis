package io.dropwizard.redis.uri;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.google.common.net.HostAndPort;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.util.Duration;
import io.lettuce.core.RedisURI;
import org.junit.jupiter.api.Test;

import java.io.File;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class RedisModeURIFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<RedisURIFactory> configFactory =
            new YamlConfigurationFactory<>(RedisURIFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildARedisURI() throws Exception {
        final File yml = new File(Resources.getResource("yaml/uri/redis-uri.yaml").toURI());
        final RedisURIFactory factory = configFactory.build(yml);
        assertThat(factory)
                .isInstanceOf(RedisModeURIFactory.class);

        final RedisModeURIFactory redisModeURIFactory = (RedisModeURIFactory) factory;

        assertThat(redisModeURIFactory.getNode())
                .isEqualTo(HostAndPort.fromParts("127.0.0.1", 6379));
        assertThat(redisModeURIFactory.getTimeout())
                .isEqualTo(Duration.seconds(90));
        assertThat(redisModeURIFactory.getClientName())
                .isEqualTo("test");
        assertThat(redisModeURIFactory.getPassword())
                .isEqualTo("hunter2");
        assertThat(redisModeURIFactory.isSsl())
                .isTrue();
        assertThat(redisModeURIFactory.isStartTls())
                .isTrue();
        assertThat(redisModeURIFactory.isVerifyPeer())
                .isTrue();

        assertThat(factory.build())
                .isInstanceOf(RedisURI.class);
    }

    @Test
    public void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(RedisModeURIFactory.class);
    }
}
