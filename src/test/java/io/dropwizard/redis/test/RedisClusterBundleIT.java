package io.dropwizard.redis.test;

import com.github.fppt.jedismock.RedisServer;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.lettuce.core.api.StatefulRedisConnection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class RedisClusterBundleIT {
    private static final String CONFIG_PATH = "src/test/resources/yaml/config.yaml";

    private static final String REDIS_NODE_KEY = "redis.node.node";

    private static DropwizardTestSupport<TestConfiguration> APP_RULE;

    private static RedisServer REDIS;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        REDIS = RedisServer.newRedisServer();  // bind to a random port
        REDIS.start();

        APP_RULE = new DropwizardTestSupport<>(TestApplication.class, CONFIG_PATH,
                ConfigOverride.config(REDIS_NODE_KEY, String.format("%s:%d", REDIS.getHost(), REDIS.getBindPort())));
        APP_RULE.before();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        APP_RULE.after();
        REDIS.stop();
    }

    @Test
    public void shouldHavePinged() {
        final StatefulRedisConnection<String, String> connection = ((TestApplication) APP_RULE.getApplication()).getRedisCluster()
                .getConnection();

        assertThat(connection.sync().get("foo"))
                .isEqualTo("bar");
    }
}
