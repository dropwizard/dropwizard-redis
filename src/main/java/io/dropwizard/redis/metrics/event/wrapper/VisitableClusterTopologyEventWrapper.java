package io.dropwizard.redis.metrics.event.wrapper;

import io.dropwizard.redis.metrics.event.visitor.EventVisitor;
import io.lettuce.core.cluster.event.ClusterTopologyChangedEvent;

import static java.util.Objects.requireNonNull;

public class VisitableClusterTopologyEventWrapper implements VisitableEventWrapper {
    private final ClusterTopologyChangedEvent event;

    public VisitableClusterTopologyEventWrapper(final ClusterTopologyChangedEvent event) {
        this.event = requireNonNull(event);
    }

    @Override
    public void accept(final EventVisitor visitor) {
        visitor.visit(event);
    }
}
