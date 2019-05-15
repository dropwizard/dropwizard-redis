package io.dropwizard.redis.security.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.net.SocketFactory;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultSSLSocketFactoryFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<SSLSocketFactoryFactory> factory =
            new YamlConfigurationFactory<>(SSLSocketFactoryFactory.class, validator, objectMapper, "dw");

    @Test
    public void isDiscoverable() throws Exception {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(DefaultSSLSocketFactoryFactory.class);
    }

    @Test
    public void shouldBuildDefaultSSLSocketFactory() throws URISyntaxException, IOException, ConfigurationException {
        final File yaml = new File(Resources.getResource("yaml/security/socket/default.yaml").toURI());
        final SSLSocketFactoryFactory factory = this.factory.build(yaml);
        assertThat(factory).isInstanceOf(DefaultSSLSocketFactoryFactory.class);

        assertThat(factory.build()).isInstanceOf(SocketFactory.class);
    }
}
