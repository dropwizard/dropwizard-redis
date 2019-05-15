package io.dropwizard.redis.security.hostnameverifier;

import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.net.ssl.HostnameVerifier;

@JsonTypeName("noop")
public class NoopHostnameVerifierFactory implements HostnameVerifierFactory {
    @Override
    public HostnameVerifier build() {
        return (hostname, session) -> true;
    }
}
