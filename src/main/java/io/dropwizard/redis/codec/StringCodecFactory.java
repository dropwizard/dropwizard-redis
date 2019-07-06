package io.dropwizard.redis.codec;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;

@JsonTypeName("string")
public class StringCodecFactory implements RedisCodecFactory<String, String> {
    @Override
    public RedisCodec<String, String> build() {
        return new StringCodec();
    }
}
