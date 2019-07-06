package io.dropwizard.redis.codec;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;

@JsonTypeName("byte-array")
public class ByteArrayCodecFactory implements RedisCodecFactory<byte[], byte[]> {
    @Override
    public RedisCodec<byte[], byte[]> build() {
        return new ByteArrayCodec();
    }
}
