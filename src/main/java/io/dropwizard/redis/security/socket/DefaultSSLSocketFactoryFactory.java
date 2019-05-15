package io.dropwizard.redis.security.socket;

import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

@JsonTypeName("default")
public class DefaultSSLSocketFactoryFactory implements SSLSocketFactoryFactory {
    @Override
    public SocketFactory build() {
        return SSLSocketFactory.getDefault();
    }
}
