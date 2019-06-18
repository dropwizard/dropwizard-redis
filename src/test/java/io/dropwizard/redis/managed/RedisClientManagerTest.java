package io.dropwizard.redis.managed;

import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.api.StatefulConnection;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

public class RedisClientManagerTest {
    private static final String NAME = "redis";

    private final AbstractRedisClient client = mock(AbstractRedisClient.class);
    private final StatefulConnection connection = mock(StatefulConnection.class);

    private final RedisClientManager redisClientManager = new RedisClientManager(client, connection, NAME);

    @Before
    public void setUp() throws Exception {
        reset(client, connection);
    }

    @Test
    public void stopShouldCloseClient() throws Exception {
        // when
        redisClientManager.stop();

        // then
        verify(connection).close();
        verify(client).shutdown();
    }
}
