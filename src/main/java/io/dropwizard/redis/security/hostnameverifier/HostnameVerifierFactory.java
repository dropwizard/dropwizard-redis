package io.dropwizard.redis.security.hostnameverifier;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

import javax.net.ssl.HostnameVerifier;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface HostnameVerifierFactory extends Discoverable {
    HostnameVerifier build();
}
