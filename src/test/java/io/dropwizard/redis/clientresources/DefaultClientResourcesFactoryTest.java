package io.dropwizard.redis.clientresources;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.redis.delay.ExponentialDelayFactory;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.junit.jupiter.api.Test;

import java.io.File;

import jakarta.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultClientResourcesFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<ClientResourcesFactory> configFactory =
            new YamlConfigurationFactory<>(ClientResourcesFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildClientResources() throws Exception {
        final File yml = new File(Resources.getResource("yaml/clientresources/default.yaml").toURI());
        final ClientResourcesFactory factory = configFactory.build(yml);
        assertThat(factory)
                .isInstanceOf(DefaultClientResourcesFactory.class);

        final DefaultClientResourcesFactory defaultClientResourcesFactory = (DefaultClientResourcesFactory) factory;
        assertThat(defaultClientResourcesFactory.getCommandLatencyRecorder())
                .isNotNull();
        assertThat(defaultClientResourcesFactory.getEventPublisherOptions())
                .isNotNull();
        assertThat(defaultClientResourcesFactory.getEventBusFactory())
                .isNotNull();
        assertThat(defaultClientResourcesFactory.getEventExecutorGroup())
                .isNotNull();
        assertThat(defaultClientResourcesFactory.getEventLoopGroupProvider())
                .isNotNull();
        assertThat(defaultClientResourcesFactory.getComputationThreads())
                .isEqualTo(DefaultClientResources.DEFAULT_COMPUTATION_THREADS);
        assertThat(defaultClientResourcesFactory.getIoThreads())
                .isEqualTo(DefaultClientResources.DEFAULT_IO_THREADS);
        assertThat(defaultClientResourcesFactory.getDelay())
                .isInstanceOf(ExponentialDelayFactory.class);

        assertThat(defaultClientResourcesFactory.build("name", new MetricRegistry(), null))
                .isInstanceOf(ClientResources.class);
    }

    @Test
    public void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(DefaultClientResourcesFactory.class);
    }
}
