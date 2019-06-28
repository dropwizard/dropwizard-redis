package io.dropwizard.redis.clientoptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import org.junit.Test;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class ClusterClientOptionsFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<ClusterClientOptionsFactory> configFactory =
            new YamlConfigurationFactory<>(ClusterClientOptionsFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildClusterClientOptions() throws Exception {
        final ClusterClientOptionsFactory factory = configFactory.build();

        assertThat(factory.getSslOptions())
                .isNull();
        assertThat(factory.getMaxRedirects())
                .isEqualTo(ClusterClientOptions.DEFAULT_MAX_REDIRECTS);
        assertThat(factory.getTopologyRefreshOptions())
                .isNull();
        assertThat(factory.isAutoReconnect())
                .isEqualTo(ClusterClientOptions.DEFAULT_AUTO_RECONNECT);
        assertThat(factory.isValidateClusterNodeMembership())
                .isEqualTo(ClusterClientOptions.DEFAULT_VALIDATE_CLUSTER_MEMBERSHIP);
        assertThat(factory.isCancelCommandsOnReconnectFailure())
                .isEqualTo(ClusterClientOptions.DEFAULT_CANCEL_CMD_RECONNECT_FAIL);
        assertThat(factory.isPingBeforeActivateConnection())
                .isEqualTo(ClusterClientOptions.DEFAULT_PING_BEFORE_ACTIVATE_CONNECTION);
        assertThat(factory.isPublishOnScheduler())
                .isEqualTo(ClusterClientOptions.DEFAULT_PUBLISH_ON_SCHEDULER);
        assertThat(factory.isSuspendReconnectOnProtocolFailure())
                .isEqualTo(ClusterClientOptions.DEFAULT_SUSPEND_RECONNECT_PROTO_FAIL);
        assertThat(factory.getDisconnectedBehavior())
                .isEqualTo(ClientOptions.DEFAULT_DISCONNECTED_BEHAVIOR);
        assertThat(factory.getRequestQueueSize())
                .isEqualTo(ClusterClientOptions.DEFAULT_REQUEST_QUEUE_SIZE);
        assertThat(factory.getSocketOptions())
                .isNotNull();
        assertThat(factory.getTimeoutOptions())
                .isNotNull();

        assertThat(factory.build()).isNotNull();
    }
}
