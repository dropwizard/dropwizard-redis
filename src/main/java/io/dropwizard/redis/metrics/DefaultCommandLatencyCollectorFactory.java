package io.dropwizard.redis.metrics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.validation.MinSize;
import io.lettuce.core.metrics.DefaultCommandLatencyCollector;
import io.lettuce.core.metrics.DefaultCommandLatencyCollectorOptions;
import org.hibernate.validator.constraints.Length;

import java.util.concurrent.TimeUnit;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonTypeName("default")
public class DefaultCommandLatencyCollectorFactory implements CommandLatencyCollectorFactory {
    @NotNull
    @JsonProperty
    private TimeUnit targetUnit = DefaultCommandLatencyCollectorOptions.DEFAULT_TARGET_UNIT;

    @NotNull
    @Size(min = 1)
    @JsonProperty
    private double[] targetPercentiles = DefaultCommandLatencyCollectorOptions.DEFAULT_TARGET_PERCENTILES;

    @JsonProperty
    private boolean resetLatenciesAfterEvent = DefaultCommandLatencyCollectorOptions.DEFAULT_RESET_LATENCIES_AFTER_EVENT;

    @JsonProperty
    private boolean localDistinction = DefaultCommandLatencyCollectorOptions.DEFAULT_LOCAL_DISTINCTION;

    @JsonProperty
    private boolean enabled = DefaultCommandLatencyCollectorOptions.DEFAULT_ENABLED;

    public TimeUnit getTargetUnit() {
        return targetUnit;
    }

    public void setTargetUnit(final TimeUnit targetUnit) {
        this.targetUnit = targetUnit;
    }

    public double[] getTargetPercentiles() {
        return targetPercentiles;
    }

    public void setTargetPercentiles(final double[] targetPercentiles) {
        this.targetPercentiles = targetPercentiles;
    }

    public boolean isResetLatenciesAfterEvent() {
        return resetLatenciesAfterEvent;
    }

    public void setResetLatenciesAfterEvent(final boolean resetLatenciesAfterEvent) {
        this.resetLatenciesAfterEvent = resetLatenciesAfterEvent;
    }

    public boolean isLocalDistinction() {
        return localDistinction;
    }

    public void setLocalDistinction(final boolean localDistinction) {
        this.localDistinction = localDistinction;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public DefaultCommandLatencyCollector build() {
        final DefaultCommandLatencyCollectorOptions.Builder builder = DefaultCommandLatencyCollectorOptions.builder()
                .targetUnit(targetUnit)
                .targetPercentiles(targetPercentiles)
                .resetLatenciesAfterEvent(resetLatenciesAfterEvent)
                .localDistinction(localDistinction);

        if (enabled) {
            builder.enable();
        } else {
            builder.disable();
        }

        return new DefaultCommandLatencyCollector(builder.build());
    }
}
