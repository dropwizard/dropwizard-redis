package io.dropwizard.redis.metrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.util.Duration;
import io.lettuce.core.event.DefaultEventPublisherOptions;
import org.junit.jupiter.api.Test;

import java.io.File;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultEventPublisherOptionsFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<EventPublisherOptionsFactory> configFactory =
            new YamlConfigurationFactory<>(EventPublisherOptionsFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildDefaultEventPublisherOptions() throws Exception {
        final File yml = new File(Resources.getResource("yaml/metrics/default-event-publisher.yaml").toURI());
        final EventPublisherOptionsFactory factory = configFactory.build(yml);
        assertThat(factory)
                .isInstanceOf(DefaultEventPublisherOptionsFactory.class);

        final DefaultEventPublisherOptionsFactory defaultEventPublisherOptionsFactory = (DefaultEventPublisherOptionsFactory) factory;
        assertThat(defaultEventPublisherOptionsFactory.getEventEmitInterval())
                .isEqualTo(Duration.seconds(5));

        assertThat(defaultEventPublisherOptionsFactory.build())
                .isInstanceOf(DefaultEventPublisherOptions.class);
    }

    @Test
    public void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(DefaultEventPublisherOptionsFactory.class);
    }
}
