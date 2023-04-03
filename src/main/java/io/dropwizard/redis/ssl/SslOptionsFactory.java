package io.dropwizard.redis.ssl;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.lettuce.core.SslOptions;
import io.netty.handler.ssl.SslProvider;

import java.io.File;

import jakarta.validation.constraints.NotNull;

public class SslOptionsFactory {
    @JsonProperty
    private boolean enabled = true;

    @NotNull
    @JsonProperty
    private SslProvider sslProvider = SslOptions.DEFAULT_SSL_PROVIDER;

    @NotNull
    @JsonProperty
    private File keystore;

    @JsonProperty
    private String keystorePassword;

    @NotNull
    @JsonProperty
    private File truststore;

    @JsonProperty
    private String truststorePassword;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public SslProvider getSslProvider() {
        return sslProvider;
    }

    public void setSslProvider(final SslProvider sslProvider) {
        this.sslProvider = sslProvider;
    }

    public File getKeystore() {
        return keystore;
    }

    public void setKeystore(final File keystore) {
        this.keystore = keystore;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(final String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public File getTruststore() {
        return truststore;
    }

    public void setTruststore(final File truststore) {
        this.truststore = truststore;
    }

    public String getTruststorePassword() {
        return truststorePassword;
    }

    public void setTruststorePassword(final String truststorePassword) {
        this.truststorePassword = truststorePassword;
    }

    public SslOptions build() {
        final SslOptions.Builder builder = SslOptions.builder();

        switch (sslProvider) {
            case JDK:
                builder.jdkSslProvider();
                break;
            case OPENSSL:
            case OPENSSL_REFCNT:
                builder.openSslProvider();
                break;
        }

        if (keystorePassword != null) {
            builder.keystore(keystore, keystorePassword.toCharArray());
        } else {
            builder.keystore(keystore);
        }

        if (truststorePassword != null) {
            builder.truststore(truststore, truststorePassword);
        } else {
            builder.truststore(truststore);
        }

        return builder.build();
    }
}
