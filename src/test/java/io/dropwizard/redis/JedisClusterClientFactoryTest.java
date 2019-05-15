package io.dropwizard.redis;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.redis.pool.JedisPoolConfigFactory;
import io.dropwizard.util.Duration;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.JedisCluster;

import java.io.File;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

public class JedisClusterClientFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<RedisClusterClientFactory> configFactory =
            new YamlConfigurationFactory<>(RedisClusterClientFactory.class, validator, objectMapper, "dw");

    private final MetricRegistry metrics = mock(MetricRegistry.class);
    private final HealthCheckRegistry healthChecks = mock(HealthCheckRegistry.class);
    private final LifecycleEnvironment lifecycle = mock(LifecycleEnvironment.class);

    @Before
    public void setUp() throws Exception {
        reset(metrics, healthChecks, lifecycle);
    }

    @Test
    public void isDiscoverable() throws Exception {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(JedisClusterClientFactory.class);
    }

    @Test
    public void shouldBuildJedisClusterFromYaml() throws Exception {
        final File yml = new File(Resources.getResource("yaml/jedis-cluster.yaml").toURI());
        final RedisClusterClientFactory factory = configFactory.build(yml);

        assertThat(factory).isInstanceOf(JedisClusterClientFactory.class);

        assertThat(factory.getName()).isEqualTo("dev");
        assertThat(factory.isMetricsEnabled()).isTrue();
        assertThat(factory.getPool()).isInstanceOf(   JedisPoolConfigFactory.class);
        assertThat(factory.getConnectionTimeout()).isEqualTo(Duration.seconds(2));
        assertThat(factory.getSocketTimeout()).isEqualTo(Duration.seconds(2));
        assertThat(factory.getPassword()).isEqualTo("hunter2");
        assertThat(factory.getMaxAttempts()).isEqualTo(5);

        final JedisCluster config = (JedisCluster) factory.build(metrics, healthChecks, lifecycle);
        assertThat(config).isNotNull();
    }
}
