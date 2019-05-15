package io.dropwizard.redis.security.socket;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

import javax.net.SocketFactory;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface SSLSocketFactoryFactory extends Discoverable {
    SocketFactory build();
}
