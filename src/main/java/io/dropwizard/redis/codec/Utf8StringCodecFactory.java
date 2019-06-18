package io.dropwizard.redis.codec;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.Utf8StringCodec;

@JsonTypeName("utf8-string")
public class Utf8StringCodecFactory implements RedisCodecFactory<String, String> {
    @Override
    public RedisCodec<String, String> build() {
        return new Utf8StringCodec();
    }
}
