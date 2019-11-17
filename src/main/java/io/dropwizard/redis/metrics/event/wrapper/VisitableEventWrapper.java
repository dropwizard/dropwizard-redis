package io.dropwizard.redis.metrics.event.wrapper;

import io.dropwizard.redis.metrics.event.visitor.EventVisitor;

public interface VisitableEventWrapper {
    void accept(EventVisitor visitor);
}
