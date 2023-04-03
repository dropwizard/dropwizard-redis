package io.dropwizard.redis.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.util.Duration;
import io.lettuce.core.SocketOptions;
import org.junit.jupiter.api.Test;

import java.io.File;

import jakarta.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class SocketOptionsFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<SocketOptionsFactory> configFactory =
            new YamlConfigurationFactory<>(SocketOptionsFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildASocketOptions() throws Exception {
        final File yml = new File(Resources.getResource("yaml/socket/socket-options.yaml").toURI());
        final SocketOptionsFactory factory = configFactory.build(yml);

        assertThat(factory.getConnectTimeout())
                .isEqualTo(Duration.seconds(5));
        assertThat(factory.isKeepAlive())
                .isTrue();
        assertThat(factory.isTcpNoDelay())
                .isTrue();

        assertThat(factory.build())
                .isInstanceOf(SocketOptions.class);
    }
}
