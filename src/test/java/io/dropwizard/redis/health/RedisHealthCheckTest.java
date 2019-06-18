package io.dropwizard.redis.health;

import io.lettuce.core.RedisException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class RedisHealthCheckTest {
    @Mock
    private Pingable client = mock(Pingable.class);

    private RedisHealthCheck healthCheck = new RedisHealthCheck(client);

    @Before
    public void setUp() {
        reset(client);
    }

    @Test
    public void shouldReturnHealthyIfPingSucceeds() {
        when(client.ping()).thenReturn(RedisHealthCheck.HEALTHY_STRING);

        assertThat(healthCheck.check().isHealthy(), is(true));
    }

    @Test
    public void shouldReturnUnhealthyIfPingFails() {
        when(client.ping()).thenReturn("???");

        assertThat(healthCheck.check().isHealthy(), is(false));
    }

    @Test
    public void shouldReturnUnhealthyIfPingThrowsException() {
        when(client.ping()).thenThrow(new RedisException("failed for some reason"));
        assertThat(healthCheck.check().isHealthy(), is(false));
    }
}
