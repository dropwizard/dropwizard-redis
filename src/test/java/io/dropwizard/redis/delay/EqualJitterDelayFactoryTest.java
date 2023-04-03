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

import jakarta.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class EqualJitterDelayFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<DelayFactory> configFactory =
            new YamlConfigurationFactory<>(DelayFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildAnEqualJitterDelayFactory() throws Exception {
        final File yml = new File(Resources.getResource("yaml/delay/equal-jitter.yaml").toURI());
        final DelayFactory factory = configFactory.build(yml);
        assertThat(factory)
                .isInstanceOf(EqualJitterDelayFactory.class);

        final EqualJitterDelayFactory delayFactory = (EqualJitterDelayFactory) factory;
        assertThat(delayFactory.getBase()).isEqualTo(1L);
        assertThat(delayFactory.getLowerBound()).isEqualTo(Duration.seconds(1));
        assertThat(delayFactory.getUpperBound()).isEqualTo(Duration.seconds(5));
    }

    @Test
    public void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(EqualJitterDelayFactory.class);
    }
}
