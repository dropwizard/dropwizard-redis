package io.dropwizard.redis.event;

import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultEventBusFactoryTest {
    @Test
    public void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(DefaultEventBusFactory.class);
    }
}
