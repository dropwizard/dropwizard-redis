package io.dropwizard.redis.metrics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.util.Duration;
import io.lettuce.core.event.DefaultEventPublisherOptions;
import io.lettuce.core.event.EventPublisherOptions;

import jakarta.validation.constraints.NotNull;

@JsonTypeName("default")
public class DefaultEventPublisherOptionsFactory implements EventPublisherOptionsFactory {
    @NotNull
    @JsonProperty
    private Duration eventEmitInterval = Duration.minutes(DefaultEventPublisherOptions.DEFAULT_EMIT_INTERVAL);

    public Duration getEventEmitInterval() {
        return eventEmitInterval;
    }

    public void setEventEmitInterval(final Duration eventEmitInterval) {
        this.eventEmitInterval = eventEmitInterval;
    }

    @Override
    public EventPublisherOptions build() {
        return DefaultEventPublisherOptions.builder()
                .eventEmitInterval(java.time.Duration.ofSeconds(eventEmitInterval.toSeconds()))
                .build();
    }
}
