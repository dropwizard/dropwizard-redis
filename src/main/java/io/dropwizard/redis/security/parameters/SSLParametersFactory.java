package io.dropwizard.redis.security.parameters;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.security.AlgorithmConstraints;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.RegEx;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SSLParameters;
import javax.validation.constraints.NotNull;

public class SSLParametersFactory {
    @JsonProperty
    private List<String> cipherSuites;
    @JsonProperty
    private List<String> protocols;
    @JsonProperty
    private boolean wantClientAuth = false;
    @JsonProperty
    private boolean needClientAuth = false;
    @JsonProperty
    private String identificationAlgorithm;
    @JsonProperty
    private AlgorithmConstraints algorithmConstraints;
    @NotNull
    @JsonProperty
    private List<String> sniHostNames = Collections.emptyList();
    @RegEx
    @NotNull
    @JsonProperty
    private List<String> sniMatchers = Collections.emptyList();
    @JsonProperty
    private boolean preferLocalCipherSuites;
    // TODO: Java 9+ configs
//    @JsonProperty
//    private boolean enableRetransmissions = true;
//    @Min(0)
//    @JsonProperty
//    private int maximumPacketSize = 0;
//    @JsonProperty
//    private List<String> applicationProtocols = Collections.emptyList();

    public List<String> getCipherSuites() {
        return cipherSuites;
    }

    public void setCipherSuites(final List<String> cipherSuites) {
        this.cipherSuites = cipherSuites;
    }

    public List<String> getProtocols() {
        return protocols;
    }

    public void setProtocols(final List<String> protocols) {
        this.protocols = protocols;
    }

    public boolean isWantClientAuth() {
        return wantClientAuth;
    }

    public void setWantClientAuth(final boolean wantClientAuth) {
        this.wantClientAuth = wantClientAuth;
    }

    public boolean isNeedClientAuth() {
        return needClientAuth;
    }

    public void setNeedClientAuth(final boolean needClientAuth) {
        this.needClientAuth = needClientAuth;
    }

    public String getIdentificationAlgorithm() {
        return identificationAlgorithm;
    }

    public void setIdentificationAlgorithm(final String identificationAlgorithm) {
        this.identificationAlgorithm = identificationAlgorithm;
    }

    public AlgorithmConstraints getAlgorithmConstraints() {
        return algorithmConstraints;
    }

    public void setAlgorithmConstraints(final AlgorithmConstraints algorithmConstraints) {
        this.algorithmConstraints = algorithmConstraints;
    }

    public List<String> getSniHostNames() {
        return sniHostNames;
    }

    public void setSniHostNames(final List<String> sniHostNames) {
        this.sniHostNames = sniHostNames;
    }

    public List<String> getSniMatchers() {
        return sniMatchers;
    }

    public void setSniMatchers(final List<String> sniMatchers) {
        this.sniMatchers = sniMatchers;
    }

    public boolean isPreferLocalCipherSuites() {
        return preferLocalCipherSuites;
    }

    public void setPreferLocalCipherSuites(final boolean preferLocalCipherSuites) {
        this.preferLocalCipherSuites = preferLocalCipherSuites;
    }

    public SSLParameters build() {
        final SSLParameters sslParameters = new SSLParameters(cipherSuites.toArray(new String[0]), protocols.toArray(new String[0]));
        // these setter methods clear one another, thus the if statement
        if (needClientAuth) {
            sslParameters.setNeedClientAuth(needClientAuth);
        } else if (wantClientAuth) {
            sslParameters.setWantClientAuth(wantClientAuth);
        }
        sslParameters.setEndpointIdentificationAlgorithm(identificationAlgorithm);
        sslParameters.setAlgorithmConstraints(algorithmConstraints);
        sslParameters.setSNIMatchers(sniMatchers.stream()
                .map(SNIHostName::createSNIMatcher)
                .collect(Collectors.toList()));
        sslParameters.setServerNames(sniHostNames.stream()
                .map(SNIHostName::new)
                .collect(Collectors.toList()));
        sslParameters.setUseCipherSuitesOrder(preferLocalCipherSuites);
        return sslParameters;
    }
}
