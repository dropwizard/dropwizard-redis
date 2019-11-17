package io.dropwizard.redis.metrics.event;

import com.codahale.metrics.MetricRegistry;
import com.google.common.collect.ImmutableList;
import io.dropwizard.redis.metrics.event.visitor.*;
import io.lettuce.core.cluster.event.ClusterTopologyChangedEvent;
import io.lettuce.core.event.connection.ConnectedEvent;
import io.lettuce.core.event.connection.ConnectionActivatedEvent;
import io.lettuce.core.event.connection.ConnectionDeactivatedEvent;
import io.lettuce.core.event.connection.DisconnectedEvent;
import io.lettuce.core.event.metrics.CommandLatencyEvent;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Mockito.*;

public class LettuceMetricsSubscriberTest {
    @Mock
    private MetricRegistry metrics = mock(MetricRegistry.class);

    @Test
    public void shouldDispatchEventsToListeners() {
        List<EventVisitor> visitors = ImmutableList.of(
                mock(ClusterTopologyChangedEventVisitor.class),
                mock(CommandLatencyEventVisitor.class),
                mock(ConnectedEventVisitor.class),
                mock(ConnectionActivatedEventVisitor.class),
                mock(ConnectionDeactivatedEventVisitor.class),
                mock(DisconnectedEventVisitor.class)
        );
        LettuceMetricsSubscriber subscriber = new LettuceMetricsSubscriber(visitors);

        verifyClusterTopologyChangedEvent(subscriber, visitors);
        verifyCommandLatencyEvent(subscriber, visitors);
        verifyConnectedEvent(subscriber, visitors);
        verifyConnectionActivatedEvent(subscriber, visitors);
        verifyConnectionDeactivatedEvent(subscriber, visitors);
        verifyDisconnectedEvent(subscriber, visitors);
        for (EventVisitor visitor : visitors) {
            verifyNoMoreInteractions(visitor);
        }
    }

    private void verifyClusterTopologyChangedEvent(LettuceMetricsSubscriber subscriber, List<EventVisitor> visitors) {
        ClusterTopologyChangedEvent event = mock(ClusterTopologyChangedEvent.class);
        subscriber.accept(event);

        for (EventVisitor visitor : visitors) {
            verify(visitor, times(1)).visit(event);
        }
    }

    private void verifyCommandLatencyEvent(LettuceMetricsSubscriber subscriber, List<EventVisitor> visitors) {
        CommandLatencyEvent event = mock(CommandLatencyEvent.class);
        subscriber.accept(event);

        for (EventVisitor visitor : visitors) {
            verify(visitor, times(1)).visit(event);
        }
    }

    private void verifyConnectedEvent(LettuceMetricsSubscriber subscriber, List<EventVisitor> visitors) {
        ConnectedEvent event = mock(ConnectedEvent.class);
        subscriber.accept(event);

        for (EventVisitor visitor : visitors) {
            verify(visitor, times(1)).visit(event);
        }
    }

    private void verifyConnectionActivatedEvent(LettuceMetricsSubscriber subscriber, List<EventVisitor> visitors) {
        ConnectionActivatedEvent event = mock(ConnectionActivatedEvent.class);
        subscriber.accept(event);

        for (EventVisitor visitor : visitors) {
            verify(visitor, times(1)).visit(event);
        }
    }

    private void verifyConnectionDeactivatedEvent(LettuceMetricsSubscriber subscriber, List<EventVisitor> visitors) {
        ConnectionDeactivatedEvent event = mock(ConnectionDeactivatedEvent.class);
        subscriber.accept(event);

        for (EventVisitor visitor : visitors) {
            verify(visitor, times(1)).visit(event);
        }
    }

    private void verifyDisconnectedEvent(LettuceMetricsSubscriber subscriber, List<EventVisitor> visitors) {
        DisconnectedEvent event = mock(DisconnectedEvent.class);
        subscriber.accept(event);

        for (EventVisitor visitor : visitors) {
            verify(visitor, times(1)).visit(event);
        }
    }
}
