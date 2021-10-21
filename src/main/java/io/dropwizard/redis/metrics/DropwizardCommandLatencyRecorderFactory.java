package io.dropwizard.redis.metrics;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.lettuce.core.metrics.CommandLatencyRecorder;
import io.lettuce.core.metrics.DefaultCommandLatencyCollectorOptions;

@JsonTypeName("dropwizard")
public class DropwizardCommandLatencyRecorderFactory implements CommandLatencyRecorderFactory {

    @JsonProperty
    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public CommandLatencyRecorder build(MetricRegistry metricRegistry) {
        return new DropwizardCommandLatencyRecorder(metricRegistry, enabled);
    }
}
