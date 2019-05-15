package io.dropwizard.redis.security;

import io.dropwizard.redis.security.hostnameverifier.HostnameVerifierFactory;
import io.dropwizard.redis.security.parameters.SSLParametersFactory;
import io.dropwizard.redis.security.socket.DefaultSSLSocketFactoryFactory;
import io.dropwizard.redis.security.socket.SSLSocketFactoryFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class SecurityFactory {
    @JsonProperty
    private boolean sslEnabled = true;
    @Valid
    @NotNull
    @JsonProperty
    private SSLSocketFactoryFactory sslSocketFactory = new DefaultSSLSocketFactoryFactory();
    @JsonProperty
    private SSLParametersFactory sslParameters;
    @JsonProperty
    private HostnameVerifierFactory hostnameVerifier;

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(final boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public SSLSocketFactoryFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public void setSslSocketFactory(final SSLSocketFactoryFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    public SSLParametersFactory getSslParameters() {
        return sslParameters;
    }

    public void setSslParameters(final SSLParametersFactory sslParameters) {
        this.sslParameters = sslParameters;
    }

    public HostnameVerifierFactory getHostnameVerifier() {
        return hostnameVerifier;
    }

    public void setHostnameVerifier(final HostnameVerifierFactory hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
    }
}
