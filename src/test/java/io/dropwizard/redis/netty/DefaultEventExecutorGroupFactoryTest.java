package io.dropwizard.redis.netty;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.junit.jupiter.api.Test;

import java.io.File;

import jakarta.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultEventExecutorGroupFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<EventExecutorGroupFactory> configFactory =
            new YamlConfigurationFactory<>(EventExecutorGroupFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildADefaultEventExecutorGroup() throws Exception {
        final File yml = new File(Resources.getResource("yaml/netty/default.yaml").toURI());
        final EventExecutorGroupFactory factory = configFactory.build(yml);
        assertThat(factory)
                .isInstanceOf(DefaultEventExecutorGroupFactory.class);

        final DefaultEventExecutorGroupFactory defaultEventExecutorGroupFactory = (DefaultEventExecutorGroupFactory) factory;

        assertThat(defaultEventExecutorGroupFactory.build(5, "name", new MetricRegistry()))
                .isInstanceOf(DefaultEventExecutorGroup.class);
    }

    @Test
    public void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(DefaultEventExecutorGroupFactory.class);
    }
}
