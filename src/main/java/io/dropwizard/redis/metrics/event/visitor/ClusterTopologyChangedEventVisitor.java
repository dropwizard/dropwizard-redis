package io.dropwizard.redis.metrics.event.visitor;

import com.codahale.metrics.MetricRegistry;
import io.lettuce.core.cluster.event.ClusterTopologyChangedEvent;
import io.lettuce.core.event.connection.ConnectedEvent;
import io.lettuce.core.event.connection.ConnectionActivatedEvent;
import io.lettuce.core.event.connection.ConnectionDeactivatedEvent;
import io.lettuce.core.event.connection.DisconnectedEvent;
import io.lettuce.core.event.metrics.CommandLatencyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

public class ClusterTopologyChangedEventVisitor implements EventVisitor {
    private static final Logger log = LoggerFactory.getLogger(ClusterTopologyChangedEventVisitor.class);

    private final String name;
    private final MetricRegistry metrics;

    public ClusterTopologyChangedEventVisitor(final String name, final MetricRegistry metrics) {
        this.name = MetricRegistry.name(requireNonNull(name), "event.cluster-topology-change");
        this.metrics = requireNonNull(metrics);
    }

    @Override
    public void visit(final CommandLatencyEvent event) {
        // do nothing
    }

    @Override
    public void visit(final DisconnectedEvent event) {
        // do nothing
    }

    @Override
    public void visit(final ClusterTopologyChangedEvent event) {
        metrics.counter(name).inc();
        log.debug("Cluster topology change event occurred {}", event);
    }

    @Override
    public void visit(final ConnectedEvent event) {
        // do nothing
    }

    @Override
    public void visit(final ConnectionActivatedEvent event) {
        // do nothing
    }

    @Override
    public void visit(final ConnectionDeactivatedEvent event) {
        // do nothing
    }
}
