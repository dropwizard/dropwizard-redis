package io.dropwizard.redis.metrics.event.visitor;

import io.lettuce.core.cluster.event.ClusterTopologyChangedEvent;
import io.lettuce.core.event.connection.ConnectedEvent;
import io.lettuce.core.event.connection.ConnectionActivatedEvent;
import io.lettuce.core.event.connection.ConnectionDeactivatedEvent;
import io.lettuce.core.event.connection.DisconnectedEvent;
import io.lettuce.core.event.metrics.CommandLatencyEvent;

public interface EventVisitor {
    void visit(CommandLatencyEvent event);

    void visit(DisconnectedEvent event);

    void visit(ClusterTopologyChangedEvent event);

    void visit(ConnectedEvent event);

    void visit(ConnectionActivatedEvent event);

    void visit(ConnectionDeactivatedEvent event);
}
