package io.dropwizard.redis.topology;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.util.Duration;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import org.junit.Test;

import java.io.File;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class ClusterTopologyRefreshOptionsFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<ClusterTopologyRefreshOptionsFactory> configFactory =
            new YamlConfigurationFactory<>(ClusterTopologyRefreshOptionsFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildAClusterToplogyRefreshOptions() throws Exception {
        final File yml = new File(Resources.getResource("yaml/topology/cluster-topology-refresh-options.yaml").toURI());
        final ClusterTopologyRefreshOptionsFactory factory = configFactory.build(yml);

        assertThat(factory.isPeriodicRefreshEnabled())
                .isTrue();
        assertThat(factory.getRefreshPeriod())
                .isEqualTo(Duration.minutes(2));
        assertThat(factory.isCloseStaleConnections())
                .isFalse();
        assertThat(factory.isDynamicRefreshSources())
                .isFalse();
        assertThat(factory.getAdaptiveRefreshTriggers())
                .contains(ClusterTopologyRefreshOptions.RefreshTrigger.PERSISTENT_RECONNECTS);
        assertThat(factory.getRefreshTriggersReconnectAttempts())
                .isEqualTo(3);

        assertThat(factory.build())
                .isInstanceOf(ClusterTopologyRefreshOptions.class);
    }
}
