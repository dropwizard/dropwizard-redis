package io.dropwizard.redis.metrics;


import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import io.lettuce.core.protocol.CommandType;
import io.netty.channel.local.LocalAddress;
import org.junit.Test;

import java.net.SocketAddress;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class DropwizardCommandLatencyRecorderTest {

    private static final SocketAddress LOCAL_ADDRESS = new LocalAddress("localhost:1234");

    private static final SocketAddress REMOTE_ADDRESS = new LocalAddress("localhost:6379");


    @Test
    public void verifyMetrics() {
        MetricRegistry metricRegistry = new MetricRegistry();
        DropwizardCommandLatencyRecorder commandLatencyRecorder = new DropwizardCommandLatencyRecorder(metricRegistry, true);

        commandLatencyRecorder.recordCommandLatency(LOCAL_ADDRESS, REMOTE_ADDRESS, CommandType.SET, 10, 10);
        commandLatencyRecorder.recordCommandLatency(LOCAL_ADDRESS, REMOTE_ADDRESS, CommandType.SET, 100, 500);

        Map<String, Timer> timers = metricRegistry.getTimers();
        Timer timer = timers.get("lettuce.command.firstresponse.SET");

        assertThat(timers.size()).isEqualTo(2);
        assertThat(timer.getCount()).isEqualTo(2);
        assertThat(timer.getSnapshot().getMean()).isEqualTo(55);
    }

    @Test
    public void disabled() {
        MetricRegistry metricRegistry = new MetricRegistry();
        DropwizardCommandLatencyRecorder commandLatencyRecorder = new DropwizardCommandLatencyRecorder(metricRegistry, false);

        commandLatencyRecorder.recordCommandLatency(LOCAL_ADDRESS, REMOTE_ADDRESS, CommandType.GET, 1, 10);

        assertThat(metricRegistry.getTimers()).isEmpty();
    }
}
