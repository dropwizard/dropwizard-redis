package io.dropwizard.redis.delay;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.util.Duration;
import io.lettuce.core.resource.Delay;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonTypeName("full-jitter")
public class FullJitterDelayFactory implements DelayFactory {
    @NotNull
    @JsonProperty
    private Duration lowerBound = Duration.seconds(0);

    @NotNull
    @JsonProperty
    private Duration upperBound = Duration.seconds(30);

    @Min(0L)
    @JsonProperty
    private long base = 0L;

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

    public long getBase() {
        return base;
    }

    public void setBase(final long base) {
        this.base = base;
    }

    @Override
    public Supplier<Delay> build() {
        return () -> Delay.fullJitter(lowerBound.toMilliseconds(), upperBound.toMilliseconds(), base, TimeUnit.MILLISECONDS);
    }
}
