package io.dropwizard.redis.pool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import org.junit.Test;
import redis.clients.jedis.JedisPoolConfig;

import java.io.File;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class JedisPoolConfigFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<JedisPoolConfigFactory> configFactory =
            new YamlConfigurationFactory<>(JedisPoolConfigFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildJedisPoolConfigFromYaml() throws Exception {
        final File yml = new File(Resources.getResource("yaml/pool/jedis-pool.yaml").toURI());
        final JedisPoolConfigFactory factory = configFactory.build(yml);
        final JedisPoolConfig config = factory.build();
        assertThat(config).isInstanceOf(JedisPoolConfig.class);
        assertThat(config.getMaxTotal()).isEqualTo(20);
        assertThat(config.getMinIdle()).isEqualTo(5);
        assertThat(config.getMaxIdle()).isEqualTo(20);
        assertThat(config.getJmxEnabled()).isFalse();
        assertThat(config.getJmxNameBase()).isEqualTo("base");
        assertThat(config.getJmxNamePrefix()).isEqualTo("prefix");
        assertThat(config.getBlockWhenExhausted()).isFalse();
        assertThat(config.getTimeBetweenEvictionRunsMillis()).isEqualTo(10_000L);
        assertThat(config.getTestWhileIdle()).isTrue();
        assertThat(config.getTestOnReturn()).isTrue();
        assertThat(config.getTestOnBorrow()).isTrue();
        assertThat(config.getTestOnCreate()).isTrue();
        assertThat(config.getMinEvictableIdleTimeMillis()).isEqualTo(5_000L);
        assertThat(config.getSoftMinEvictableIdleTimeMillis()).isEqualTo(1_000L);
        assertThat(config.getMaxWaitMillis()).isEqualTo(10_000L);
        assertThat(config.getNumTestsPerEvictionRun()).isEqualTo(4);
        assertThat(config.getEvictionPolicyClassName()).isEqualTo(JedisPoolConfig.DEFAULT_EVICTION_POLICY_CLASS_NAME);
        assertThat(config.getEvictorShutdownTimeoutMillis()).isEqualTo(10_000L);
        assertThat(config.getFairness()).isTrue();
        assertThat(config.getLifo()).isFalse();
    }

    @Test
    public void buildsJedisPoolWithDefaults() throws Exception {
        final JedisPoolConfigFactory factory = configFactory.build();
        final JedisPoolConfig config = factory.build();

        assertThat(config).isInstanceOf(JedisPoolConfig.class);
        assertThat(config.getMaxTotal()).isEqualTo(8);
        assertThat(config.getMinIdle()).isEqualTo(0);
        assertThat(config.getMaxIdle()).isEqualTo(8);
    }
}
