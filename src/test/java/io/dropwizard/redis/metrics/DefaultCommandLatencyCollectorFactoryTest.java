package io.dropwizard.redis.metrics;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.lettuce.core.metrics.DefaultCommandLatencyCollector;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultCommandLatencyCollectorFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<CommandLatencyRecorderFactory> configFactory =
            new YamlConfigurationFactory<>(CommandLatencyRecorderFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildDefaultCommandLatencyCollector() throws Exception {
        final File yml = new File(Resources.getResource("yaml/metrics/default-command-latency-collector.yaml").toURI());
        final CommandLatencyRecorderFactory factory = configFactory.build(yml);
        assertThat(factory)
                .isInstanceOf(DefaultCommandLatencyCollectorFactory.class);

        final DefaultCommandLatencyCollectorFactory defaultCommandLatencyCollectorFactory = (DefaultCommandLatencyCollectorFactory) factory;
        assertThat(defaultCommandLatencyCollectorFactory.getTargetUnit())
                .isEqualTo(TimeUnit.SECONDS);
        assertThat(defaultCommandLatencyCollectorFactory.getTargetPercentiles())
                .contains(new double[] { 50.0D, 99.0D }, Offset.offset(0.01D));
        assertThat(defaultCommandLatencyCollectorFactory.isEnabled())
                .isTrue();
        assertThat(defaultCommandLatencyCollectorFactory.isLocalDistinction())
                .isTrue();
        assertThat(defaultCommandLatencyCollectorFactory.isResetLatenciesAfterEvent())
                .isTrue();

        assertThat(defaultCommandLatencyCollectorFactory.build(new MetricRegistry()))
                .isInstanceOf(DefaultCommandLatencyCollector.class);
    }

    @Test
    public void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(DefaultCommandLatencyCollectorFactory.class);
    }
}
