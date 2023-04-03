package io.dropwizard.redis.codec;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.lettuce.core.codec.CompressionCodec;
import io.lettuce.core.codec.RedisCodec;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@JsonTypeName("compression")
public class CompressionCodecFactory<K, V> implements RedisCodecFactory<K, V> {
    @Valid
    @NotNull
    @JsonProperty
    private RedisCodecFactory<K, V> delegatee;

    @NotNull
    @JsonProperty
    private CompressionCodec.CompressionType compressionType;

    public RedisCodecFactory<K, V> getDelegatee() {
        return delegatee;
    }

    public void setDelegatee(final RedisCodecFactory<K, V> delegatee) {
        this.delegatee = delegatee;
    }

    public CompressionCodec.CompressionType getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(final CompressionCodec.CompressionType compressionType) {
        this.compressionType = compressionType;
    }

    @Override
    public RedisCodec<K, V> build() {
        return CompressionCodec.valueCompressor(delegatee.build(), compressionType);
    }
}
