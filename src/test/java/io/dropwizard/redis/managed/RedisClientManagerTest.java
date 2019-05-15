package io.dropwizard.redis.managed;

import org.junit.Before;
import org.junit.Test;

import java.io.Closeable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

public class RedisClientManagerTest {
    private static final String NAME = "redis";

    private final Closeable client = mock(Closeable.class);

    private final RedisClientManager redisClientManager = new RedisClientManager(client, NAME);

    @Before
    public void setUp() throws Exception {
        reset(client);
    }

    @Test
    public void stopShouldCloseClient() throws Exception {
        // when
        redisClientManager.stop();

        // then
        verify(client).close();
    }
}
