package io.dropwizard.redis.metrics.event.wrapper;

import io.dropwizard.redis.metrics.event.visitor.EventVisitor;

public interface VisitableEventWrapper<T extends EventVisitor> {
    void accept(T visitor);
}
