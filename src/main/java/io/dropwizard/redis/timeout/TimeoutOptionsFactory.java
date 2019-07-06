package io.dropwizard.redis.timeout;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.util.Duration;
import io.lettuce.core.TimeoutOptions;

public class TimeoutOptionsFactory {
    @JsonProperty
    private boolean timeoutCommands = TimeoutOptions.DEFAULT_TIMEOUT_COMMANDS;

    @JsonProperty
    private boolean applyConnectionTimeout = false;

    @JsonProperty
    private Duration fixedTimeout;

    public boolean isTimeoutCommands() {
        return timeoutCommands;
    }

    public void setTimeoutCommands(final boolean timeoutCommands) {
        this.timeoutCommands = timeoutCommands;
    }

    public boolean isApplyConnectionTimeout() {
        return applyConnectionTimeout;
    }

    public void setApplyConnectionTimeout(final boolean applyConnectionTimeout) {
        this.applyConnectionTimeout = applyConnectionTimeout;
    }

    public Duration getFixedTimeout() {
        return fixedTimeout;
    }

    public void setFixedTimeout(final Duration fixedTimeout) {
        this.fixedTimeout = fixedTimeout;
    }

    public TimeoutOptions build() {
        final TimeoutOptions.Builder builder = TimeoutOptions.builder()
                .timeoutCommands(timeoutCommands);

        if (applyConnectionTimeout) {
            builder.connectionTimeout();
        }

        if (fixedTimeout != null) {
            builder.fixedTimeout(java.time.Duration.ofMillis(fixedTimeout.toMilliseconds()));
        }

        return builder.build();
    }
}
