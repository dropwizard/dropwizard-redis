package io.dropwizard.redis.security.hostnameverifier;

import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

@JsonTypeName("default")
public class DefaultHostnameVerifierFactory implements HostnameVerifierFactory {
    @Override
    public HostnameVerifier build() {
        return HttpsURLConnection.getDefaultHostnameVerifier();
    }
}
