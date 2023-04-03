package io.dropwizard.redis.delay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.util.Duration;
import org.junit.jupiter.api.Test;

import java.io.File;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class FullJitterDelayFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<DelayFactory> configFactory =
            new YamlConfigurationFactory<>(DelayFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildAFullJitterDelayFactory() throws Exception {
        final File yml = new File(Resources.getResource("yaml/delay/full-jitter.yaml").toURI());
        final DelayFactory factory = configFactory.build(yml);
        assertThat(factory)
                .isInstanceOf(FullJitterDelayFactory.class);

        final FullJitterDelayFactory delayFactory = (FullJitterDelayFactory) factory;
        assertThat(delayFactory.getBase()).isEqualTo(1L);
        assertThat(delayFactory.getLowerBound()).isEqualTo(Duration.seconds(1));
        assertThat(delayFactory.getUpperBound()).isEqualTo(Duration.seconds(5));
    }

    @Test
    public void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(FullJitterDelayFactory.class);
    }
}
