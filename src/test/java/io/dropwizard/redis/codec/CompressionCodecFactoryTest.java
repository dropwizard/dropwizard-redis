package io.dropwizard.redis.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.lettuce.core.codec.CompressionCodec;
import io.lettuce.core.codec.RedisCodec;
import org.junit.jupiter.api.Test;

import java.io.File;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class CompressionCodecFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<RedisCodecFactory> configFactory =
            new YamlConfigurationFactory<>(RedisCodecFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildACompressionCodec() throws Exception {
        final File yml = new File(Resources.getResource("yaml/codec/compression.yaml").toURI());
        final RedisCodecFactory factory = configFactory.build(yml);
        assertThat(factory)
                .isInstanceOf(CompressionCodecFactory.class);

        final CompressionCodecFactory compressionCodecFactory = (CompressionCodecFactory) factory;
        assertThat(compressionCodecFactory.getCompressionType())
                .isEqualTo(CompressionCodec.CompressionType.GZIP);
        assertThat(compressionCodecFactory.getDelegatee())
                .isInstanceOf(StringCodecFactory.class);
        assertThat(compressionCodecFactory.build())
                .isInstanceOf(RedisCodec.class);
    }

    @Test
    public void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(CompressionCodecFactory.class);
    }
}
