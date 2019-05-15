package io.dropwizard.redis.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.redis.security.hostnameverifier.DefaultHostnameVerifierFactory;
import io.dropwizard.redis.security.parameters.SSLParametersFactory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class SecurityFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<SecurityFactory> factory =
            new YamlConfigurationFactory<>(SecurityFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildSecurityFactory() throws URISyntaxException, IOException, ConfigurationException {
        final File yaml = new File(Resources.getResource("yaml/security/security.yaml").toURI());
        final SecurityFactory factory = this.factory.build(yaml);

        assertThat(factory.isSslEnabled()).isTrue();
        assertThat(factory.getHostnameVerifier()).isInstanceOf(DefaultHostnameVerifierFactory.class);
        assertThat(factory.getSslParameters()).isInstanceOf(SSLParametersFactory.class);
    }
}
