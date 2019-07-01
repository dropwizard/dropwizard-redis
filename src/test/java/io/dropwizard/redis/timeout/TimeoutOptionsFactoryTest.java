package io.dropwizard.redis.timeout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.util.Duration;
import io.lettuce.core.TimeoutOptions;
import org.junit.Test;

import java.io.File;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeoutOptionsFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<TimeoutOptionsFactory> configFactory =
            new YamlConfigurationFactory<>(TimeoutOptionsFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildATimeoutOptions() throws Exception {
        final File yml = new File(Resources.getResource("yaml/timeout/timeout-options.yaml").toURI());
        final TimeoutOptionsFactory factory = configFactory.build(yml);

        assertThat(factory.getFixedTimeout())
                .isEqualTo(Duration.seconds(6));
        assertThat(factory.isTimeoutCommands())
                .isTrue();
        assertThat(factory.isApplyConnectionTimeout())
                .isTrue();

        assertThat(factory.build())
                .isInstanceOf(TimeoutOptions.class);
    }
}
