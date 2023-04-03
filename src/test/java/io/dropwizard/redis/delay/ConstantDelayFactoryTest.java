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

public class ConstantDelayFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<DelayFactory> configFactory =
            new YamlConfigurationFactory<>(DelayFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildAConstantDelayFactory() throws Exception {
        final File yml = new File(Resources.getResource("yaml/delay/constant.yaml").toURI());
        final DelayFactory factory = configFactory.build(yml);
        assertThat(factory)
                .isInstanceOf(ConstantDelayFactory.class);

        final ConstantDelayFactory constantDelayFactory = (ConstantDelayFactory) factory;
        assertThat(constantDelayFactory.getDuration())
                .isEqualTo(Duration.seconds(5));
    }

    @Test
    public void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(ConstantDelayFactory.class);
    }
}
