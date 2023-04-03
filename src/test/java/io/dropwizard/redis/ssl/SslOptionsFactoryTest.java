package io.dropwizard.redis.ssl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.lettuce.core.SslOptions;
import org.junit.jupiter.api.Test;

import java.io.File;

import jakarta.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class SslOptionsFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<SslOptionsFactory> configFactory =
            new YamlConfigurationFactory<>(SslOptionsFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldSslOptions() throws Exception {
        final File yml = new File(Resources.getResource("yaml/ssl/ssl.yaml").toURI());
        final SslOptionsFactory factory = configFactory.build(yml);

        assertThat(factory.isEnabled())
                .isTrue();
        assertThat(factory.getSslProvider())
                .isEqualTo(SslOptions.DEFAULT_SSL_PROVIDER);
        assertThat(factory.getKeystore())
                .isFile();
        assertThat(factory.getKeystorePassword())
                .isEqualTo("hunter2");
        assertThat(factory.getTruststore())
                .isFile();
        assertThat(factory.getTruststorePassword())
                .isEqualTo("hunter2");

        assertThat(factory.build()).isNotNull();
    }
}
