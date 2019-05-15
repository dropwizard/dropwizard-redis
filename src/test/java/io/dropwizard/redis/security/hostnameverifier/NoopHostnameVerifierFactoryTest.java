package io.dropwizard.redis.security.hostnameverifier;

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

import javax.net.ssl.HostnameVerifier;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class NoopHostnameVerifierFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<HostnameVerifierFactory> factory =
            new YamlConfigurationFactory<>(HostnameVerifierFactory.class, validator, objectMapper, "dw");

    @Test
    public void isDiscoverable() throws Exception {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(NoopHostnameVerifierFactory.class);
    }

    @Test
    public void shouldBuildNoopHostnameVerifier() throws URISyntaxException, IOException, ConfigurationException {
        final File yaml = new File(Resources.getResource("yaml/security/hostnameverifier/noop.yaml").toURI());
        final HostnameVerifierFactory factory = this.factory.build(yaml);
        assertThat(factory).isInstanceOf(NoopHostnameVerifierFactory.class);

        assertThat(factory.build()).isInstanceOf(HostnameVerifier.class);
    }
}
