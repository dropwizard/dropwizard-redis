package io.dropwizard.redis.delay;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.util.Duration;
import io.lettuce.core.resource.Delay;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonTypeName("exponential")
public class ExponentialDelayFactory implements DelayFactory {
    @NotNull
    @JsonProperty
    private Duration lowerBound = Duration.seconds(0);

    @NotNull
    @JsonProperty
    private Duration upperBound = Duration.seconds(30);

    @Min(2)
    @JsonProperty
    private int powersOf = 2;

    public Duration getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(final Duration lowerBound) {
        this.lowerBound = lowerBound;
    }

    public Duration getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(final Duration upperBound) {
        this.upperBound = upperBound;
    }

    public int getPowersOf() {
        return powersOf;
    }

    public void setPowersOf(final int powersOf) {
        this.powersOf = powersOf;
    }

    @Override
    public Supplier<Delay> build() {
        return () -> Delay.exponential(java.time.Duration.ofMillis(lowerBound.toMilliseconds()),
                java.time.Duration.ofMillis(upperBound.toMilliseconds()),
                powersOf,
                TimeUnit.MILLISECONDS);
    }
}
