package io.dropwizard.redis.security.parameters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.net.ssl.SSLParameters;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class SSLParametersFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<SSLParametersFactory> factory =
            new YamlConfigurationFactory<>(SSLParametersFactory.class, validator, objectMapper, "dw");
    @Test
    public void shouldBuildSSLParameters() throws URISyntaxException, IOException, ConfigurationException {
        final File yaml = new File(Resources.getResource("yaml/security/parameters/ssl.yaml").toURI());
        final SSLParametersFactory factory = this.factory.build(yaml);
        assertThat(factory.getCipherSuites()).contains("abc", "123");
        assertThat(factory.getProtocols()).containsOnly("protocol1");
        assertThat(factory.isWantClientAuth()).isFalse();
        assertThat(factory.isNeedClientAuth()).isTrue();
        assertThat(factory.getSniHostNames()).containsOnly("localhost");
        assertThat(factory.getSniMatchers()).isEmpty();
        assertThat(factory.isPreferLocalCipherSuites()).isTrue();

        assertThat(factory.build()).isInstanceOf(SSLParameters.class);
    }
}
