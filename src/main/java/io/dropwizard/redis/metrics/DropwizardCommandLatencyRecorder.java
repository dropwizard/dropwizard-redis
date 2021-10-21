package io.dropwizard.redis.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import io.lettuce.core.metrics.CommandLatencyId;
import io.lettuce.core.metrics.CommandLatencyRecorder;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import io.lettuce.core.internal.LettuceAssert;
import io.lettuce.core.protocol.ProtocolKeyword;
import io.netty.channel.local.LocalAddress;

import static com.codahale.metrics.MetricRegistry.name;

public class DropwizardCommandLatencyRecorder implements CommandLatencyRecorder {

    static final String METRIC_COMPLETION = "lettuce.command.completion";

    static final String METRIC_FIRST_RESPONSE = "lettuce.command.firstresponse";

    private final MetricRegistry metricRegistry;

    private final boolean isEnabled;

    private final Map<CommandLatencyId, Timer> completionTimers = new ConcurrentHashMap<>();

    private final Map<CommandLatencyId, Timer> firstResponseTimers = new ConcurrentHashMap<>();


    public DropwizardCommandLatencyRecorder(MetricRegistry metricRegistry, boolean isEnabled) {
        LettuceAssert.notNull(metricRegistry, "MetricRegistry must not be null");

        this.metricRegistry = metricRegistry;
        this.isEnabled = isEnabled;
    }

    @Override
    public void recordCommandLatency(SocketAddress local, SocketAddress remote, ProtocolKeyword protocolKeyword,
                                     long firstResponseLatency, long completionLatency) {
        if (!isEnabled()) {
            return;
        }

        CommandLatencyId commandLatencyId = CommandLatencyId.create(local, remote, protocolKeyword);

        Timer firstResponseTimer = firstResponseTimers.computeIfAbsent(commandLatencyId, c -> metricRegistry
                .timer(name(METRIC_FIRST_RESPONSE, c.commandType().name())));
        firstResponseTimer.update(firstResponseLatency, TimeUnit.NANOSECONDS);

        Timer completionTimer = completionTimers.computeIfAbsent(commandLatencyId, c -> metricRegistry
                .timer(name(METRIC_COMPLETION, c.commandType().name())));
        completionTimer.update(completionLatency, TimeUnit.NANOSECONDS);
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
