package io.dropwizard.redis.delay;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.util.Duration;
import io.lettuce.core.resource.Delay;

import java.util.function.Supplier;

import javax.validation.constraints.NotNull;

@JsonTypeName("constant")
public class ConstantDelayFactory implements DelayFactory {
    @NotNull
    @JsonProperty
    private Duration duration;

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(final Duration duration) {
        this.duration = duration;
    }

    @Override
    public Supplier<Delay> build() {
        final Delay delay = Delay.constant(java.time.Duration.ofMillis(duration.toMilliseconds()));
        return () -> delay;
    }
}
