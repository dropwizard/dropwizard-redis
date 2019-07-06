package io.dropwizard.redis.event;

import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultEventLoopGroupProviderFactoryTest {
    @Test
    public void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(DefaultEventLoopGroupProviderFactory.class);
    }
}
