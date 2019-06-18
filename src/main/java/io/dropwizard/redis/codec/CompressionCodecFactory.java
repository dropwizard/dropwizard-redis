package io.dropwizard.redis.codec;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.lettuce.core.codec.CompressionCodec;
import io.lettuce.core.codec.RedisCodec;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonTypeName("compression")
public class CompressionCodecFactory<K, V> implements RedisCodecFactory<K, V> {
    @Valid
    @NotNull
    @JsonProperty
    private RedisCodecFactory<K, V> delegatee;

    @NotNull
    @JsonProperty
    private CompressionCodec.CompressionType compressionType;

    @Override
    public RedisCodec<K, V> build() {
        return CompressionCodec.valueCompressor(delegatee.build(), compressionType);
    }
}
