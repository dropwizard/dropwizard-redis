package io.dropwizard.redis.codec;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;
import io.lettuce.core.codec.RedisCodec;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface RedisCodecFactory<K, V> extends Discoverable {
    RedisCodec<K, V> build();
}
