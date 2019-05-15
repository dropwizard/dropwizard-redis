package io.dropwizard.redis;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import io.opentracing.contrib.redis.common.RedisCommand;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.ListPosition;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;
import redis.clients.jedis.params.GeoRadiusParam;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.params.ZAddParams;
import redis.clients.jedis.params.ZIncrByParams;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * TODO: This should ideally be temporary until Jedis supports command listeners, or exposes metrics.
 * See: https://github.com/xetorthio/jedis/pull/1972
 */
class InstrumentedJedisCluster extends JedisCluster {
    private final MetricRegistry metrics;
    private final String name;

    public InstrumentedJedisCluster(final HostAndPort node, final MetricRegistry metrics, final String name) {
        super(node);
        this.metrics = requireNonNull(metrics);
        this.name = requireNonNull(name);
    }

    public InstrumentedJedisCluster(final HostAndPort node, final int timeout, final MetricRegistry metrics, final String name) {
        super(node, timeout);
        this.metrics = requireNonNull(metrics);
        this.name = requireNonNull(name);
    }

    public InstrumentedJedisCluster(final HostAndPort node, final int timeout, final int maxAttempts, final MetricRegistry metrics,
                                    final String name) {
        super(node, timeout, maxAttempts);
        this.metrics = requireNonNull(metrics);
        this.name = requireNonNull(name);
    }

    public InstrumentedJedisCluster(final HostAndPort node, final GenericObjectPoolConfig poolConfig, final MetricRegistry metrics, final String name) {
        super(node, poolConfig);
        this.metrics = requireNonNull(metrics);
        this.name = requireNonNull(name);
    }

    public InstrumentedJedisCluster(final HostAndPort node, final int timeout, final GenericObjectPoolConfig poolConfig, final MetricRegistry metrics, final String name) {
        super(node, timeout, poolConfig);
        this.metrics = requireNonNull(metrics);
        this.name = requireNonNull(name);
    }

    public InstrumentedJedisCluster(final HostAndPort node, final int timeout, final int maxAttempts,
                                    final GenericObjectPoolConfig poolConfig, final MetricRegistry metrics, final String name) {
        super(node, timeout, maxAttempts, poolConfig);
        this.metrics = requireNonNull(metrics);
        this.name = requireNonNull(name);
    }

    public InstrumentedJedisCluster(final HostAndPort node, final int connectionTimeout, final int soTimeout, final int maxAttempts,
                                    final GenericObjectPoolConfig poolConfig, final MetricRegistry metrics, final String name) {
        super(node, connectionTimeout, soTimeout, maxAttempts, poolConfig);
        this.metrics = requireNonNull(metrics);
        this.name = requireNonNull(name);
    }

    public InstrumentedJedisCluster(final HostAndPort node, final int connectionTimeout, final int soTimeout, final int maxAttempts, final String password, final GenericObjectPoolConfig poolConfig, final MetricRegistry metrics, final String name) {
        super(node, connectionTimeout, soTimeout, maxAttempts, password, poolConfig);
        this.metrics = requireNonNull(metrics);
        this.name = requireNonNull(name);
    }

    public InstrumentedJedisCluster(final HostAndPort node, final int connectionTimeout, final int soTimeout, final int maxAttempts, final String password, final String clientName, final GenericObjectPoolConfig poolConfig, final MetricRegistry metrics, final String name) {
        super(node, connectionTimeout, soTimeout, maxAttempts, password, clientName, poolConfig);
        this.metrics = requireNonNull(metrics);
        this.name = requireNonNull(name);
    }

    public InstrumentedJedisCluster(final Set<HostAndPort> nodes, final MetricRegistry metrics, final String name) {
        super(nodes);
        this.metrics = requireNonNull(metrics);
        this.name = requireNonNull(name);
    }

    public InstrumentedJedisCluster(final Set<HostAndPort> nodes, final int timeout, final MetricRegistry metrics, final String name) {
        super(nodes, timeout);
        this.metrics = requireNonNull(metrics);
        this.name = requireNonNull(name);
    }

    public InstrumentedJedisCluster(final Set<HostAndPort> nodes, final int timeout, final int maxAttempts, final MetricRegistry metrics,
                                    final String name) {
        super(nodes, timeout, maxAttempts);
        this.metrics = requireNonNull(metrics);
        this.name = requireNonNull(name);
    }

    public InstrumentedJedisCluster(final Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig, final MetricRegistry metrics,
                                    final String name) {
        super(nodes, poolConfig);
        this.metrics = requireNonNull(metrics);
        this.name = requireNonNull(name);
    }

    public InstrumentedJedisCluster(final Set<HostAndPort> nodes, final int timeout, final GenericObjectPoolConfig poolConfig,
                                    final MetricRegistry metrics, final String name) {
        super(nodes, timeout, poolConfig);
        this.metrics = requireNonNull(metrics);
        this.name = requireNonNull(name);
    }

    public InstrumentedJedisCluster(final Set<HostAndPort> jedisClusterNode, final int timeout, final int maxAttempts,
                                    final GenericObjectPoolConfig poolConfig, final MetricRegistry metrics, final String name) {
        super(jedisClusterNode, timeout, maxAttempts, poolConfig);
        this.metrics = requireNonNull(metrics);
        this.name = requireNonNull(name);
    }

    public InstrumentedJedisCluster(final Set<HostAndPort> jedisClusterNode, final int connectionTimeout, final int soTimeout,
                                    final int maxAttempts, final GenericObjectPoolConfig poolConfig, final MetricRegistry metrics,
                                    final String name) {
        super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, poolConfig);
        this.metrics = requireNonNull(metrics);
        this.name = requireNonNull(name);
    }

    public InstrumentedJedisCluster(final Set<HostAndPort> jedisClusterNode, final int connectionTimeout, final int soTimeout,
                                    final int maxAttempts, final String password, final GenericObjectPoolConfig poolConfig, final MetricRegistry metrics, final String name) {
        super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, password, poolConfig);
        this.metrics = requireNonNull(metrics);
        this.name = requireNonNull(name);
    }

    public InstrumentedJedisCluster(final Set<HostAndPort> jedisClusterNode, final int connectionTimeout, final int soTimeout, final int maxAttempts, final String password, final String clientName, final GenericObjectPoolConfig poolConfig, final MetricRegistry metrics, final String name) {
        super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, password, clientName, poolConfig);
        this.metrics = requireNonNull(metrics);
        this.name = requireNonNull(name);
    }

    @Override
    public String set(final String key, final String value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SET)).time()) {
            return super.set(key, value);
        }
    }

    @Override
    public String set(final String key, final String value, final SetParams params) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SET)).time()) {
            return super.set(key, value, params);
        }
    }

    @Override
    public String get(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GET)).time()) {
            return super.get(key);
        }
    }

    @Override
    public Boolean exists(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.EXISTS)).time()) {
            return super.exists(key);
        }
    }

    @Override
    public Long exists(final String... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.EXISTS)).time()) {
            return super.exists(keys);
        }
    }

    @Override
    public Long persist(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PERSIST)).time()) {
            return super.persist(key);
        }
    }

    @Override
    public String type(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.TYPE)).time()) {
            return super.type(key);
        }
    }

    @Override
    public byte[] dump(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.DUMP)).time()) {
            return super.dump(key);
        }
    }

    @Override
    public String restore(final String key, final int ttl, final byte[] serializedValue) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.RESTORE)).time()) {
            return super.restore(key, ttl, serializedValue);
        }
    }

    @Override
    public Long expire(final String key, final int seconds) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.EXPIRE)).time()) {
            return super.expire(key, seconds);
        }
    }

    @Override
    public Long pexpire(final String key, final long milliseconds) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PEXPIRE)).time()) {
            return super.pexpire(key, milliseconds);
        }
    }

    @Override
    public Long expireAt(final String key, final long unixTime) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.EXPIREAT)).time()) {
            return super.expireAt(key, unixTime);
        }
    }

    @Override
    public Long pexpireAt(final String key, final long millisecondsTimestamp) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PEXPIREAT)).time()) {
            return super.pexpireAt(key, millisecondsTimestamp);
        }
    }

    @Override
    public Long ttl(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.TTL)).time()) {
            return super.ttl(key);
        }
    }

    @Override
    public Long pttl(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PTTL)).time()) {
            return super.pttl(key);
        }
    }

    @Override
    public Long touch(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.DUMP)).time()) {
            return super.touch(key);
        }
    }

    @Override
    public Long touch(final String... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.TOUCH)).time()) {
            return super.touch(keys);
        }
    }

    @Override
    public Boolean setbit(final String key, final long offset, final boolean value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SETBIT)).time()) {
            return super.setbit(key, offset, value);
        }
    }

    @Override
    public Boolean setbit(final String key, final long offset, final String value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SETBIT)).time()) {
            return super.setbit(key, offset, value);
        }
    }

    @Override
    public Boolean getbit(final String key, final long offset) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GETBIT)).time()) {
            return super.getbit(key, offset);
        }
    }

    @Override
    public Long setrange(final String key, final long offset, final String value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SETRANGE)).time()) {
            return super.setrange(key, offset, value);
        }
    }

    @Override
    public String getrange(final String key, final long startOffset, final long endOffset) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GETRANGE)).time()) {
            return super.getrange(key, startOffset, endOffset);
        }
    }

    @Override
    public String getSet(final String key, final String value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GETSET)).time()) {
            return super.getSet(key, value);
        }
    }

    @Override
    public Long setnx(final String key, final String value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SETNX)).time()) {
            return super.setnx(key, value);
        }
    }

    @Override
    public String setex(final String key, final int seconds, final String value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SETEX)).time()) {
            return super.setex(key, seconds, value);
        }
    }

    @Override
    public String psetex(final String key, final long milliseconds, final String value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PSETEX)).time()) {
            return super.psetex(key, milliseconds, value);
        }
    }

    @Override
    public Long decrBy(final String key, final long decrement) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.DECRBY)).time()) {
            return super.decrBy(key, decrement);
        }
    }

    @Override
    public Long decr(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.DECR)).time()) {
            return super.decr(key);
        }
    }

    @Override
    public Long incrBy(final String key, final long increment) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.INCRBY)).time()) {
            return super.incrBy(key, increment);
        }
    }

    @Override
    public Double incrByFloat(final String key, final double increment) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.INCRBYFLOAT)).time()) {
            return super.incrByFloat(key, increment);
        }
    }

    @Override
    public Long incr(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.INCR)).time()) {
            return super.incr(key);
        }
    }

    @Override
    public Long append(final String key, final String value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.APPEND)).time()) {
            return super.append(key, value);
        }
    }

    @Override
    public String substr(final String key, final int start, final int end) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, "SUBSTR")).time()) {
            return super.substr(key, start, end);
        }
    }

    @Override
    public Long hset(final String key, final String field, final String value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HSET)).time()) {
            return super.hset(key, field, value);
        }
    }

    @Override
    public Long hset(final String key, final Map<String, String> hash) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HSET)).time()) {
            return super.hset(key, hash);
        }
    }

    @Override
    public String hget(final String key, final String field) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HGET)).time()) {
            return super.hget(key, field);
        }
    }

    @Override
    public Long hsetnx(final String key, final String field, final String value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HSETNX)).time()) {
            return super.hsetnx(key, field, value);
        }
    }

    @Override
    public String hmset(final String key, final Map<String, String> hash) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HMSET)).time()) {
            return super.hmset(key, hash);
        }
    }

    @Override
    public List<String> hmget(final String key, final String... fields) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HMGET)).time()) {
            return super.hmget(key, fields);
        }
    }

    @Override
    public Long hincrBy(final String key, final String field, final long value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HINCRBY)).time()) {
            return super.hincrBy(key, field, value);
        }
    }

    @Override
    public Boolean hexists(final String key, final String field) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HEXISTS)).time()) {
            return super.hexists(key, field);
        }
    }

    @Override
    public Long hdel(final String key, final String... field) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HDEL)).time()) {
            return super.hdel(key, field);
        }
    }

    @Override
    public Long hlen(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HLEN)).time()) {
            return super.hlen(key);
        }
    }

    @Override
    public Set<String> hkeys(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HKEYS)).time()) {
            return super.hkeys(key);
        }
    }

    @Override
    public List<String> hvals(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HVALS)).time()) {
            return super.hvals(key);
        }
    }

    @Override
    public Map<String, String> hgetAll(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HGETALL)).time()) {
            return super.hgetAll(key);
        }
    }

    @Override
    public Long rpush(final String key, final String... string) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.RPUSH)).time()) {
            return super.rpush(key, string);
        }
    }

    @Override
    public Long lpush(final String key, final String... string) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.LPUSH)).time()) {
            return super.lpush(key, string);
        }
    }

    @Override
    public Long llen(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.LLEN)).time()) {
            return super.llen(key);
        }
    }

    @Override
    public List<String> lrange(final String key, final long start, final long stop) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.LRANGE)).time()) {
            return super.lrange(key, start, stop);
        }
    }

    @Override
    public String ltrim(final String key, final long start, final long stop) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.LTRIM)).time()) {
            return super.ltrim(key, start, stop);
        }
    }

    @Override
    public String lindex(final String key, final long index) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.LINDEX)).time()) {
            return super.lindex(key, index);
        }
    }

    @Override
    public String lset(final String key, final long index, final String value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.LSET)).time()) {
            return super.lset(key, index, value);
        }
    }

    @Override
    public Long lrem(final String key, final long count, final String value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.LREM)).time()) {
            return super.lrem(key, count, value);
        }
    }

    @Override
    public String lpop(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.LPOP)).time()) {
            return super.lpop(key);
        }
    }

    @Override
    public String rpop(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.RPOP)).time()) {
            return super.rpop(key);
        }
    }

    @Override
    public Long sadd(final String key, final String... member) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SADD)).time()) {
            return super.sadd(key, member);
        }
    }

    @Override
    public Set<String> smembers(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SMEMBERS)).time()) {
            return super.smembers(key);
        }
    }

    @Override
    public Long srem(final String key, final String... member) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SREM)).time()) {
            return super.srem(key, member);
        }
    }

    @Override
    public String spop(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SPOP)).time()) {
            return super.spop(key);
        }
    }

    @Override
    public Set<String> spop(final String key, final long count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SPOP)).time()) {
            return super.spop(key, count);
        }
    }

    @Override
    public Long scard(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SCARD)).time()) {
            return super.scard(key);
        }
    }

    @Override
    public Boolean sismember(final String key, final String member) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SISMEMBER)).time()) {
            return super.sismember(key, member);
        }
    }

    @Override
    public String srandmember(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SRANDMEMBER)).time()) {
            return super.srandmember(key);
        }
    }

    @Override
    public List<String> srandmember(final String key, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SRANDMEMBER)).time()) {
            return super.srandmember(key, count);
        }
    }

    @Override
    public Long strlen(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.STRLEN)).time()) {
            return super.strlen(key);
        }
    }

    @Override
    public Long zadd(final String key, final double score, final String member) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZADD)).time()) {
            return super.zadd(key, score, member);
        }
    }

    @Override
    public Long zadd(final String key, final double score, final String member, final ZAddParams params) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZADD)).time()) {
            return super.zadd(key, score, member, params);
        }
    }

    @Override
    public Long zadd(final String key, final Map<String, Double> scoreMembers) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZADD)).time()) {
            return super.zadd(key, scoreMembers);
        }
    }

    @Override
    public Long zadd(final String key, final Map<String, Double> scoreMembers, final ZAddParams params) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZADD)).time()) {
            return super.zadd(key, scoreMembers, params);
        }
    }

    @Override
    public Set<String> zrange(final String key, final long start, final long stop) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGE)).time()) {
            return super.zrange(key, start, stop);
        }
    }

    @Override
    public Long zrem(final String key, final String... members) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREM)).time()) {
            return super.zrem(key, members);
        }
    }

    @Override
    public Double zincrby(final String key, final double increment, final String member) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZINCRBY)).time()) {
            return super.zincrby(key, increment, member);
        }
    }

    @Override
    public Double zincrby(final String key, final double increment, final String member, final ZIncrByParams params) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZINCRBY)).time()) {
            return super.zincrby(key, increment, member, params);
        }
    }

    @Override
    public Long zrank(final String key, final String member) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANK)).time()) {
            return super.zrank(key, member);
        }
    }

    @Override
    public Long zrevrank(final String key, final String member) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANK)).time()) {
            return super.zrevrank(key, member);
        }
    }

    @Override
    public Set<String> zrevrange(final String key, final long start, final long stop) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGE)).time()) {
            return super.zrevrange(key, start, stop);
        }
    }

    @Override
    public Set<Tuple> zrangeWithScores(final String key, final long start, final long stop) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGE_WITHSCORES)).time()) {
            return super.zrangeWithScores(key, start, stop);
        }
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(final String key, final long start, final long stop) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGE_WITHSCORES)).time()) {
            return super.zrevrangeWithScores(key, start, stop);
        }
    }

    @Override
    public Long zcard(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZCARD)).time()) {
            return super.zcard(key);
        }
    }

    @Override
    public Double zscore(final String key, final String member) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZSCORE)).time()) {
            return super.zscore(key, member);
        }
    }

    @Override
    public List<String> sort(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SORT)).time()) {
            return super.sort(key);
        }
    }

    @Override
    public List<String> sort(final String key, final SortingParams sortingParameters) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SORT)).time()) {
            return super.sort(key, sortingParameters);
        }
    }

    @Override
    public Long zcount(final String key, final double min, final double max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZCOUNT)).time()) {
            return super.zcount(key, min, max);
        }
    }

    @Override
    public Long zcount(final String key, final String min, final String max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZCOUNT)).time()) {
            return super.zcount(key, min, max);
        }
    }

    @Override
    public Set<String> zrangeByScore(final String key, final double min, final double max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGEBYSCORE)).time()) {
            return super.zrangeByScore(key, min, max);
        }
    }

    @Override
    public Set<String> zrangeByScore(final String key, final String min, final String max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGEBYSCORE)).time()) {
            return super.zrangeByScore(key, min, max);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(final String key, final double max, final double min) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYSCORE)).time()) {
            return super.zrevrangeByScore(key, max, min);
        }
    }

    @Override
    public Set<String> zrangeByScore(final String key, final double min, final double max, final int offset, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGEBYSCORE)).time()) {
            return super.zrangeByScore(key, min, max, offset, count);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(final String key, final String max, final String min) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYSCORE)).time()) {
            return super.zrevrangeByScore(key, max, min);
        }
    }

    @Override
    public Set<String> zrangeByScore(final String key, final String min, final String max, final int offset, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGEBYSCORE)).time()) {
            return super.zrangeByScore(key, min, max, offset, count);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(final String key, final double max, final double min, final int offset, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYSCORE)).time()) {
            return super.zrevrangeByScore(key, max, min, offset, count);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGEBYSCORE_WITHSCORES)).time()) {
            return super.zrangeByScoreWithScores(key, min, max);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYSCORE_WITHSCORES)).time()) {
            return super.zrevrangeByScoreWithScores(key, max, min);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max, final int offset, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGEBYSCORE_WITHSCORES)).time()) {
            return super.zrangeByScoreWithScores(key, min, max, offset, count);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(final String key, final String max, final String min, final int offset, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYSCORE)).time()) {
            return super.zrevrangeByScore(key, max, min, offset, count);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGEBYSCORE_WITHSCORES)).time()) {
            return super.zrangeByScoreWithScores(key, min, max);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYSCORE_WITHSCORES)).time()) {
            return super.zrevrangeByScoreWithScores(key, max, min);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max, final int offset, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGEBYSCORE_WITHSCORES)).time()) {
            return super.zrangeByScoreWithScores(key, min, max, offset, count);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min, final int offset, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYSCORE_WITHSCORES)).time()) {
            return super.zrevrangeByScoreWithScores(key, max, min, offset, count);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min, final int offset, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYSCORE_WITHSCORES)).time()) {
            return super.zrevrangeByScoreWithScores(key, max, min, offset, count);
        }
    }

    @Override
    public Long zremrangeByRank(final String key, final long start, final long stop) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREMRANGEBYSCORE)).time()) {
            return super.zremrangeByRank(key, start, stop);
        }
    }

    @Override
    public Long zremrangeByScore(final String key, final double min, final double max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREMRANGEBYSCORE)).time()) {
            return super.zremrangeByScore(key, min, max);
        }
    }

    @Override
    public Long zremrangeByScore(final String key, final String min, final String max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREMRANGEBYSCORE)).time()) {
            return super.zremrangeByScore(key, min, max);
        }
    }

    @Override
    public Long zlexcount(final String key, final String min, final String max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZLEXCOUNT)).time()) {
            return super.zlexcount(key, min, max);
        }
    }

    @Override
    public Set<String> zrangeByLex(final String key, final String min, final String max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGEBYLEX)).time()) {
            return super.zrangeByLex(key, min, max);
        }
    }

    @Override
    public Set<String> zrangeByLex(final String key, final String min, final String max, final int offset, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGEBYLEX)).time()) {
            return super.zrangeByLex(key, min, max, offset, count);
        }
    }

    @Override
    public Set<String> zrevrangeByLex(final String key, final String max, final String min) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYLEX)).time()) {
            return super.zrevrangeByLex(key, max, min);
        }
    }

    @Override
    public Set<String> zrevrangeByLex(final String key, final String max, final String min, final int offset, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYLEX)).time()) {
            return super.zrevrangeByLex(key, max, min, offset, count);
        }
    }

    @Override
    public Long zremrangeByLex(final String key, final String min, final String max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREMRANGEBYLEX)).time()) {
            return super.zremrangeByLex(key, min, max);
        }
    }

    @Override
    public Long linsert(final String key, final ListPosition where, final String pivot, final String value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.LINSERT)).time()) {
            return super.linsert(key, where, pivot, value);
        }
    }

    @Override
    public Long lpushx(final String key, final String... string) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.LPUSHX)).time()) {
            return super.lpushx(key, string);
        }
    }

    @Override
    public Long rpushx(final String key, final String... string) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.RPUSHX)).time()) {
            return super.rpushx(key, string);
        }
    }

    @Override
    public Long del(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.DEL)).time()) {
            return super.del(key);
        }
    }

    @Override
    public Long unlink(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.UNLINK)).time()) {
            return super.unlink(key);
        }
    }

    @Override
    public Long unlink(final String... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.UNLINK)).time()) {
            return super.unlink(keys);
        }
    }

    @Override
    public String echo(final String string) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ECHO)).time()) {
            return super.echo(string);
        }
    }

    @Override
    public Long bitcount(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.BITCOUNT)).time()) {
            return super.bitcount(key);
        }
    }

    @Override
    public Long bitcount(final String key, final long start, final long end) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.BITCOUNT)).time()) {
            return super.bitcount(key, start, end);
        }
    }

    @Override
    public Set<String> keys(final String pattern) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.KEYS)).time()) {
            return super.keys(pattern);
        }
    }

    @Override
    public ScanResult<String> scan(final String cursor, final ScanParams params) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SCAN)).time()) {
            return super.scan(cursor, params);
        }
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HSCAN)).time()) {
            return super.hscan(key, cursor);
        }
    }

    @Override
    public ScanResult<String> sscan(final String key, final String cursor) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SSCAN)).time()) {
            return super.sscan(key, cursor);
        }
    }

    @Override
    public ScanResult<Tuple> zscan(final String key, final String cursor) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZSCAN)).time()) {
            return super.zscan(key, cursor);
        }
    }

    @Override
    public Long pfadd(final String key, final String... elements) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PFADD)).time()) {
            return super.pfadd(key, elements);
        }
    }

    @Override
    public long pfcount(final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PFCOUNT)).time()) {
            return super.pfcount(key);
        }
    }

    @Override
    public List<String> blpop(final int timeout, final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.BLPOP)).time()) {
            return super.blpop(timeout, key);
        }
    }

    @Override
    public List<String> brpop(final int timeout, final String key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.BRPOP)).time()) {
            return super.brpop(timeout, key);
        }
    }

    @Override
    public Long del(final String... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.DEL)).time()) {
            return super.del(keys);
        }
    }

    @Override
    public List<String> blpop(final int timeout, final String... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.BLPOP)).time()) {
            return super.blpop(timeout, keys);
        }
    }

    @Override
    public List<String> brpop(final int timeout, final String... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.BRPOP)).time()) {
            return super.brpop(timeout, keys);
        }
    }

    @Override
    public List<String> mget(final String... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.MGET)).time()) {
            return super.mget(keys);
        }
    }

    @Override
    public String mset(final String... keysvalues) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.MSET)).time()) {
            return super.mset(keysvalues);
        }
    }

    @Override
    public Long msetnx(final String... keysvalues) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.MSETNX)).time()) {
            return super.msetnx(keysvalues);
        }
    }

    @Override
    public String rename(final String oldkey, final String newkey) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.RENAME)).time()) {
            return super.rename(oldkey, newkey);
        }
    }

    @Override
    public Long renamenx(final String oldkey, final String newkey) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.RENAMENX)).time()) {
            return super.renamenx(oldkey, newkey);
        }
    }

    @Override
    public String rpoplpush(final String srckey, final String dstkey) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.RPOPLPUSH)).time()) {
            return super.rpoplpush(srckey, dstkey);
        }
    }

    @Override
    public Set<String> sdiff(final String... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SDIFF)).time()) {
            return super.sdiff(keys);
        }
    }

    @Override
    public Long sdiffstore(final String dstkey, final String... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SDIFFSTORE)).time()) {
            return super.sdiffstore(dstkey, keys);
        }
    }

    @Override
    public Set<String> sinter(final String... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SINTER)).time()) {
            return super.sinter(keys);
        }
    }

    @Override
    public Long sinterstore(final String dstkey, final String... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SINTERSTORE)).time()) {
            return super.sinterstore(dstkey, keys);
        }
    }

    @Override
    public Long smove(final String srckey, final String dstkey, final String member) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SMOVE)).time()) {
            return super.smove(srckey, dstkey, member);
        }
    }

    @Override
    public Long sort(final String key, final SortingParams sortingParameters, final String dstkey) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SORT)).time()) {
            return super.sort(key, sortingParameters, dstkey);
        }
    }

    @Override
    public Long sort(final String key, final String dstkey) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SORT)).time()) {
            return super.sort(key, dstkey);
        }
    }

    @Override
    public Set<String> sunion(final String... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SUNION)).time()) {
            return super.sunion(keys);
        }
    }

    @Override
    public Long sunionstore(final String dstkey, final String... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SUNIONSTORE)).time()) {
            return super.sunionstore(dstkey, keys);
        }
    }

    @Override
    public Long zinterstore(final String dstkey, final String... sets) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZINTERSTORE)).time()) {
            return super.zinterstore(dstkey, sets);
        }
    }

    @Override
    public Long zinterstore(final String dstkey, final ZParams params, final String... sets) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZINTERSTORE)).time()) {
            return super.zinterstore(dstkey, params, sets);
        }
    }

    @Override
    public Long zunionstore(final String dstkey, final String... sets) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZUNIONSTORE)).time()) {
            return super.zunionstore(dstkey, sets);
        }
    }

    @Override
    public Long zunionstore(final String dstkey, final ZParams params, final String... sets) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZUNIONSTORE)).time()) {
            return super.zunionstore(dstkey, params, sets);
        }
    }

    @Override
    public String brpoplpush(final String source, final String destination, final int timeout) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.BRPOPLPUSH)).time()) {
            return super.brpoplpush(source, destination, timeout);
        }
    }

    @Override
    public Long publish(final String channel, final String message) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PUBLISH)).time()) {
            return super.publish(channel, message);
        }
    }

    @Override
    public void subscribe(final JedisPubSub jedisPubSub, final String... channels) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SUBSCRIBE)).time()) {
            super.subscribe(jedisPubSub, channels);
        }
    }

    @Override
    public void psubscribe(final JedisPubSub jedisPubSub, final String... patterns) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PSUBSCRIBE)).time()) {
            super.psubscribe(jedisPubSub, patterns);
        }
    }

    @Override
    public Long bitop(final BitOP op, final String destKey, final String... srcKeys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.BITOP)).time()) {
            return super.bitop(op, destKey, srcKeys);
        }
    }

    @Override
    public String pfmerge(final String destkey, final String... sourcekeys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PFMERGE)).time()) {
            return super.pfmerge(destkey, sourcekeys);
        }
    }

    @Override
    public long pfcount(final String... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PFCOUNT)).time()) {
            return super.pfcount(keys);
        }
    }

    @Override
    public Object eval(final String script, final int keyCount, final String... params) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.EVAL)).time()) {
            return super.eval(script, keyCount, params);
        }
    }

    @Override
    public Object eval(final String script, final String sampleKey) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.EVAL)).time()) {
            return super.eval(script, sampleKey);
        }
    }

    @Override
    public Object eval(final String script, final List<String> keys, final List<String> args) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.EVAL)).time()) {
            return super.eval(script, keys, args);
        }
    }

    @Override
    public Object evalsha(final String sha1, final int keyCount, final String... params) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.EVALSHA)).time()) {
            return super.evalsha(sha1, keyCount, params);
        }
    }

    @Override
    public Object evalsha(final String sha1, final List<String> keys, final List<String> args) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.EVALSHA)).time()) {
            return super.evalsha(sha1, keys, args);
        }
    }

    @Override
    public Object evalsha(final String sha1, final String sampleKey) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.EVALSHA)).time()) {
            return super.evalsha(sha1, sampleKey);
        }
    }

    @Override
    public Long geoadd(final String key, final double longitude, final double latitude, final String member) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEOADD)).time()) {
            return super.geoadd(key, longitude, latitude, member);
        }
    }

    @Override
    public Long geoadd(final String key, final Map<String, GeoCoordinate> memberCoordinateMap) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEOADD)).time()) {
            return super.geoadd(key, memberCoordinateMap);
        }
    }

    @Override
    public Double geodist(final String key, final String member1, final String member2) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEODIST)).time()) {
            return super.geodist(key, member1, member2);
        }
    }

    @Override
    public Double geodist(final String key, final String member1, final String member2, final GeoUnit unit) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEODIST)).time()) {
            return super.geodist(key, member1, member2, unit);
        }
    }

    @Override
    public List<String> geohash(final String key, final String... members) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEOHASH)).time()) {
            return super.geohash(key, members);
        }
    }

    @Override
    public List<GeoCoordinate> geopos(final String key, final String... members) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEOPOS)).time()) {
            return super.geopos(key, members);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadius(final String key, final double longitude, final double latitude, final double radius,
                                             final GeoUnit unit) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEORADIUS)).time()) {
            return super.georadius(key, longitude, latitude, radius, unit);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusReadonly(final String key, final double longitude, final double latitude, final double radius,
                                                     final GeoUnit unit) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEORADIUS)).time()) {
            return super.georadiusReadonly(key, longitude, latitude, radius, unit);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadius(final String key, final double longitude, final double latitude, final double radius,
                                             final GeoUnit unit, final GeoRadiusParam param) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEORADIUS)).time()) {
            return super.georadius(key, longitude, latitude, radius, unit, param);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusReadonly(final String key, final double longitude, final double latitude, final double radius,
                                                     final GeoUnit unit, final GeoRadiusParam param) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEORADIUS)).time()) {
            return super.georadiusReadonly(key, longitude, latitude, radius, unit, param);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(final String key, final String member, final double radius, final GeoUnit unit) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEORADIUSBYMEMBER)).time()) {
            return super.georadiusByMember(key, member, radius, unit);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMemberReadonly(final String key, final String member, final double radius, final GeoUnit unit) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEORADIUSBYMEMBER)).time()) {
            return super.georadiusByMemberReadonly(key, member, radius, unit);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(final String key, final String member, final double radius, final GeoUnit unit,
                                                     final GeoRadiusParam param) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEORADIUSBYMEMBER)).time()) {
            return super.georadiusByMember(key, member, radius, unit, param);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMemberReadonly(final String key, final String member, final double radius,
                                                             final GeoUnit unit, final GeoRadiusParam param) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEORADIUSBYMEMBER)).time()) {
            return super.georadiusByMemberReadonly(key, member, radius, unit, param);
        }
    }

    @Override
    public List<Long> bitfield(final String key, final String... arguments) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.BITFIELD)).time()) {
            return super.bitfield(key, arguments);
        }
    }

    @Override
    public Long hstrlen(final String key, final String field) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HSTRLEN)).time()) {
            return super.hstrlen(key, field);
        }
    }

    @Override
    public String set(final byte[] key, final byte[] value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SET)).time()) {
            return super.set(key, value);
        }
    }

    @Override
    public String set(final byte[] key, final byte[] value, final SetParams params) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SET)).time()) {
            return super.set(key, value, params);
        }
    }

    @Override
    public byte[] get(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GET)).time()) {
            return super.get(key);
        }
    }

    @Override
    public Long exists(final byte[]... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.EXISTS)).time()) {
            return super.exists(keys);
        }
    }

    @Override
    public Boolean exists(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.EXISTS)).time()) {
            return super.exists(key);
        }
    }

    @Override
    public Long persist(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PERSIST)).time()) {
            return super.persist(key);
        }
    }

    @Override
    public String type(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.TYPE)).time()) {
            return super.type(key);
        }
    }

    @Override
    public byte[] dump(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.DUMP)).time()) {
            return super.dump(key);
        }
    }

    @Override
    public String restore(final byte[] key, final int ttl, final byte[] serializedValue) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.RESTORE)).time()) {
            return super.restore(key, ttl, serializedValue);
        }
    }

    @Override
    public Long expire(final byte[] key, final int seconds) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.EXPIRE)).time()) {
            return super.expire(key, seconds);
        }
    }

    @Override
    public Long pexpire(final byte[] key, final long milliseconds) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PEXPIRE)).time()) {
            return super.pexpire(key, milliseconds);
        }
    }

    @Override
    public Long expireAt(final byte[] key, final long unixTime) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.EXPIREAT)).time()) {
            return super.expireAt(key, unixTime);
        }
    }

    @Override
    public Long pexpireAt(final byte[] key, final long millisecondsTimestamp) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PEXPIREAT)).time()) {
            return super.pexpireAt(key, millisecondsTimestamp);
        }
    }

    @Override
    public Long ttl(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.TTL)).time()) {
            return super.ttl(key);
        }
    }

    @Override
    public Long pttl(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PTTL)).time()) {
            return super.pttl(key);
        }
    }

    @Override
    public Long touch(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.TOUCH)).time()) {
            return super.touch(key);
        }
    }

    @Override
    public Long touch(final byte[]... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.TOUCH)).time()) {
            return super.touch(keys);
        }
    }

    @Override
    public Boolean setbit(final byte[] key, final long offset, final boolean value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SETBIT)).time()) {
            return super.setbit(key, offset, value);
        }
    }

    @Override
    public Boolean setbit(final byte[] key, final long offset, final byte[] value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SETBIT)).time()) {
            return super.setbit(key, offset, value);
        }
    }

    @Override
    public Boolean getbit(final byte[] key, final long offset) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GETBIT)).time()) {
            return super.getbit(key, offset);
        }
    }

    @Override
    public Long setrange(final byte[] key, final long offset, final byte[] value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SETRANGE)).time()) {
            return super.setrange(key, offset, value);
        }
    }

    @Override
    public byte[] getrange(final byte[] key, final long startOffset, final long endOffset) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GETRANGE)).time()) {
            return super.getrange(key, startOffset, endOffset);
        }
    }

    @Override
    public byte[] getSet(final byte[] key, final byte[] value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GETSET)).time()) {
            return super.getSet(key, value);
        }
    }

    @Override
    public Long setnx(final byte[] key, final byte[] value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SETNX)).time()) {
            return super.setnx(key, value);
        }
    }

    @Override
    public String psetex(final byte[] key, final long milliseconds, final byte[] value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PSETEX)).time()) {
            return super.psetex(key, milliseconds, value);
        }
    }

    @Override
    public String setex(final byte[] key, final int seconds, final byte[] value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SETEX)).time()) {
            return super.setex(key, seconds, value);
        }
    }

    @Override
    public Long decrBy(final byte[] key, final long decrement) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.DECRBY)).time()) {
            return super.decrBy(key, decrement);
        }
    }

    @Override
    public Long decr(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.DECR)).time()) {
            return super.decr(key);
        }
    }

    @Override
    public Long incrBy(final byte[] key, final long increment) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.INCRBY)).time()) {
            return super.incrBy(key, increment);
        }
    }

    @Override
    public Double incrByFloat(final byte[] key, final double increment) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.INCRBYFLOAT)).time()) {
            return super.incrByFloat(key, increment);
        }
    }

    @Override
    public Long incr(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.INCR)).time()) {
            return super.incr(key);
        }
    }

    @Override
    public Long append(final byte[] key, final byte[] value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.APPEND)).time()) {
            return super.append(key, value);
        }
    }

    @Override
    public byte[] substr(final byte[] key, final int start, final int end) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, "SUBSTR")).time()) {
            return super.substr(key, start, end);
        }
    }

    @Override
    public Long hset(final byte[] key, final byte[] field, final byte[] value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HSET)).time()) {
            return super.hset(key, field, value);
        }
    }

    @Override
    public Long hset(final byte[] key, final Map<byte[], byte[]> hash) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HSET)).time()) {
            return super.hset(key, hash);
        }
    }

    @Override
    public byte[] hget(final byte[] key, final byte[] field) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HGET)).time()) {
            return super.hget(key, field);
        }
    }

    @Override
    public Long hsetnx(final byte[] key, final byte[] field, final byte[] value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HSETNX)).time()) {
            return super.hsetnx(key, field, value);
        }
    }

    @Override
    public String hmset(final byte[] key, final Map<byte[], byte[]> hash) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HMSET)).time()) {
            return super.hmset(key, hash);
        }
    }

    @Override
    public List<byte[]> hmget(final byte[] key, final byte[]... fields) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HMGET)).time()) {
            return super.hmget(key, fields);
        }
    }

    @Override
    public Long hincrBy(final byte[] key, final byte[] field, final long value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HINCRBY)).time()) {
            return super.hincrBy(key, field, value);
        }
    }

    @Override
    public Double hincrByFloat(final byte[] key, final byte[] field, final double value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HINCRBYFLOAT)).time()) {
            return super.hincrByFloat(key, field, value);
        }
    }

    @Override
    public Boolean hexists(final byte[] key, final byte[] field) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HEXISTS)).time()) {
            return super.hexists(key, field);
        }
    }

    @Override
    public Long hdel(final byte[] key, final byte[]... field) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HDEL)).time()) {
            return super.hdel(key, field);
        }
    }

    @Override
    public Long hlen(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HLEN)).time()) {
            return super.hlen(key);
        }
    }

    @Override
    public Set<byte[]> hkeys(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HKEYS)).time()) {
            return super.hkeys(key);
        }
    }

    @Override
    public Collection<byte[]> hvals(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HVALS)).time()) {
            return super.hvals(key);
        }
    }

    @Override
    public Map<byte[], byte[]> hgetAll(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HGETALL)).time()) {
            return super.hgetAll(key);
        }
    }

    @Override
    public Long rpush(final byte[] key, final byte[]... args) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.RPUSH)).time()) {
            return super.rpush(key, args);
        }
    }

    @Override
    public Long lpush(final byte[] key, final byte[]... args) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.LPUSH)).time()) {
            return super.lpush(key, args);
        }
    }

    @Override
    public Long llen(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.LLEN)).time()) {
            return super.llen(key);
        }
    }

    @Override
    public List<byte[]> lrange(final byte[] key, final long start, final long stop) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.LRANGE)).time()) {
            return super.lrange(key, start, stop);
        }
    }

    @Override
    public String ltrim(final byte[] key, final long start, final long stop) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.LTRIM)).time()) {
            return super.ltrim(key, start, stop);
        }
    }

    @Override
    public byte[] lindex(final byte[] key, final long index) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.LINDEX)).time()) {
            return super.lindex(key, index);
        }
    }

    @Override
    public String lset(final byte[] key, final long index, final byte[] value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.LSET)).time()) {
            return super.lset(key, index, value);
        }
    }

    @Override
    public Long lrem(final byte[] key, final long count, final byte[] value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.LREM)).time()) {
            return super.lrem(key, count, value);
        }
    }

    @Override
    public byte[] lpop(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.LPOP)).time()) {
            return super.lpop(key);
        }
    }

    @Override
    public byte[] rpop(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.RPOP)).time()) {
            return super.rpop(key);
        }
    }

    @Override
    public Long sadd(final byte[] key, final byte[]... member) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SADD)).time()) {
            return super.sadd(key, member);
        }
    }

    @Override
    public Set<byte[]> smembers(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SMEMBERS)).time()) {
            return super.smembers(key);
        }
    }

    @Override
    public Long srem(final byte[] key, final byte[]... member) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SREM)).time()) {
            return super.srem(key, member);
        }
    }

    @Override
    public byte[] spop(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SPOP)).time()) {
            return super.spop(key);
        }
    }

    @Override
    public Set<byte[]> spop(final byte[] key, final long count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SPOP)).time()) {
            return super.spop(key, count);
        }
    }

    @Override
    public Long scard(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SCARD)).time()) {
            return super.scard(key);
        }
    }

    @Override
    public Boolean sismember(final byte[] key, final byte[] member) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SISMEMBER)).time()) {
            return super.sismember(key, member);
        }
    }

    @Override
    public byte[] srandmember(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SRANDMEMBER)).time()) {
            return super.srandmember(key);
        }
    }

    @Override
    public Long strlen(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.STRLEN)).time()) {
            return super.strlen(key);
        }
    }

    @Override
    public Long zadd(final byte[] key, final double score, final byte[] member) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZADD)).time()) {
            return super.zadd(key, score, member);
        }
    }

    @Override
    public Long zadd(final byte[] key, final double score, final byte[] member, final ZAddParams params) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZADD)).time()) {
            return super.zadd(key, score, member, params);
        }
    }

    @Override
    public Long zadd(final byte[] key, final Map<byte[], Double> scoreMembers) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZADD)).time()) {
            return super.zadd(key, scoreMembers);
        }
    }

    @Override
    public Long zadd(final byte[] key, final Map<byte[], Double> scoreMembers, final ZAddParams params) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZADD)).time()) {
            return super.zadd(key, scoreMembers, params);
        }
    }

    @Override
    public Set<byte[]> zrange(final byte[] key, final long start, final long stop) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGE)).time()) {
            return super.zrange(key, start, stop);
        }
    }

    @Override
    public Long zrem(final byte[] key, final byte[]... members) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREM)).time()) {
            return super.zrem(key, members);
        }
    }

    @Override
    public Double zincrby(final byte[] key, final double increment, final byte[] member) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZINCRBY)).time()) {
            return super.zincrby(key, increment, member);
        }
    }

    @Override
    public Double zincrby(final byte[] key, final double increment, final byte[] member, final ZIncrByParams params) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZINCRBY)).time()) {
            return super.zincrby(key, increment, member, params);
        }
    }

    @Override
    public Long zrank(final byte[] key, final byte[] member) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANK)).time()) {
            return super.zrank(key, member);
        }
    }

    @Override
    public Long zrevrank(final byte[] key, final byte[] member) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANK)).time()) {
            return super.zrevrank(key, member);
        }
    }

    @Override
    public Set<byte[]> zrevrange(final byte[] key, final long start, final long stop) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGE)).time()) {
            return super.zrevrange(key, start, stop);
        }
    }

    @Override
    public Set<Tuple> zrangeWithScores(final byte[] key, final long start, final long stop) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGE_WITHSCORES)).time()) {
            return super.zrangeWithScores(key, start, stop);
        }
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(final byte[] key, final long start, final long stop) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGE_WITHSCORES)).time()) {
            return super.zrevrangeWithScores(key, start, stop);
        }
    }

    @Override
    public Long zcard(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZCARD)).time()) {
            return super.zcard(key);
        }
    }

    @Override
    public Double zscore(final byte[] key, final byte[] member) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZSCORE)).time()) {
            return super.zscore(key, member);
        }
    }

    @Override
    public List<byte[]> sort(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SORT)).time()) {
            return super.sort(key);
        }
    }

    @Override
    public List<byte[]> sort(final byte[] key, final SortingParams sortingParameters) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SORT)).time()) {
            return super.sort(key, sortingParameters);
        }
    }

    @Override
    public Long zcount(final byte[] key, final double min, final double max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZCOUNT)).time()) {
            return super.zcount(key, min, max);
        }
    }

    @Override
    public Long zcount(final byte[] key, final byte[] min, final byte[] max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZCOUNT)).time()) {
            return super.zcount(key, min, max);
        }
    }

    @Override
    public Set<byte[]> zrangeByScore(final byte[] key, final double min, final double max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGEBYSCORE)).time()) {
            return super.zrangeByScore(key, min, max);
        }
    }

    @Override
    public Set<byte[]> zrangeByScore(final byte[] key, final byte[] min, final byte[] max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGEBYSCORE)).time()) {
            return super.zrangeByScore(key, min, max);
        }
    }

    @Override
    public Set<byte[]> zrevrangeByScore(final byte[] key, final double max, final double min) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYSCORE)).time()) {
            return super.zrevrangeByScore(key, max, min);
        }
    }

    @Override
    public Set<byte[]> zrangeByScore(final byte[] key, final double min, final double max, final int offset, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGEBYSCORE)).time()) {
            return super.zrangeByScore(key, min, max, offset, count);
        }
    }

    @Override
    public Set<byte[]> zrevrangeByScore(final byte[] key, final byte[] max, final byte[] min) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYSCORE)).time()) {
            return super.zrevrangeByScore(key, max, min);
        }
    }

    @Override
    public Set<byte[]> zrangeByScore(final byte[] key, final byte[] min, final byte[] max, final int offset, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGEBYSCORE)).time()) {
            return super.zrangeByScore(key, min, max, offset, count);
        }
    }

    @Override
    public Set<byte[]> zrevrangeByScore(final byte[] key, final double max, final double min, final int offset, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYSCORE)).time()) {
            return super.zrevrangeByScore(key, max, min, offset, count);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(final byte[] key, final double min, final double max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYSCORE_WITHSCORES)).time()) {
            return super.zrangeByScoreWithScores(key, min, max);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key, final double max, final double min) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYSCORE_WITHSCORES)).time()) {
            return super.zrevrangeByScoreWithScores(key, max, min);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(final byte[] key, final double min, final double max, final int offset, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYSCORE_WITHSCORES)).time()) {
            return super.zrangeByScoreWithScores(key, min, max, offset, count);
        }
    }

    @Override
    public Set<byte[]> zrevrangeByScore(final byte[] key, final byte[] max, final byte[] min, final int offset, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYSCORE)).time()) {
            return super.zrevrangeByScore(key, max, min, offset, count);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(final byte[] key, final byte[] min, final byte[] max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGEBYSCORE_WITHSCORES)).time()) {
            return super.zrangeByScoreWithScores(key, min, max);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key, final byte[] max, final byte[] min) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYSCORE_WITHSCORES)).time()) {
            return super.zrevrangeByScoreWithScores(key, max, min);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(final byte[] key, final byte[] min, final byte[] max, final int offset, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGEBYSCORE_WITHSCORES)).time()) {
            return super.zrangeByScoreWithScores(key, min, max, offset, count);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key, final double max, final double min, final int offset, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYSCORE_WITHSCORES)).time()) {
            return super.zrevrangeByScoreWithScores(key, max, min, offset, count);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key, final byte[] max, final byte[] min, final int offset, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYSCORE_WITHSCORES)).time()) {
            return super.zrevrangeByScoreWithScores(key, max, min, offset, count);
        }
    }

    @Override
    public Long zremrangeByRank(final byte[] key, final long start, final long stop) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREMRANGEBYRANK)).time()) {
            return super.zremrangeByRank(key, start, stop);
        }
    }

    @Override
    public Long zremrangeByScore(final byte[] key, final double min, final double max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREMRANGEBYSCORE)).time()) {
            return super.zremrangeByScore(key, min, max);
        }
    }

    @Override
    public Long zremrangeByScore(final byte[] key, final byte[] min, final byte[] max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREMRANGEBYSCORE)).time()) {
            return super.zremrangeByScore(key, min, max);
        }
    }

    @Override
    public Long linsert(final byte[] key, final ListPosition where, final byte[] pivot, final byte[] value) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.LINSERT)).time()) {
            return super.linsert(key, where, pivot, value);
        }
    }

    @Override
    public Long lpushx(final byte[] key, final byte[]... arg) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.LPUSHX)).time()) {
            return super.lpushx(key, arg);
        }
    }

    @Override
    public Long rpushx(final byte[] key, final byte[]... arg) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.RPUSHX)).time()) {
            return super.rpushx(key, arg);
        }
    }

    @Override
    public Long del(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.DEL)).time()) {
            return super.del(key);
        }
    }

    @Override
    public Long unlink(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.UNLINK)).time()) {
            return super.unlink(key);
        }
    }

    @Override
    public Long unlink(final byte[]... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.UNLINK)).time()) {
            return super.unlink(keys);
        }
    }

    @Override
    public byte[] echo(final byte[] arg) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ECHO)).time()) {
            return super.echo(arg);
        }
    }

    @Override
    public Long bitcount(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.BITCOUNT)).time()) {
            return super.bitcount(key);
        }
    }

    @Override
    public Long bitcount(final byte[] key, final long start, final long end) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.BITCOUNT)).time()) {
            return super.bitcount(key, start, end);
        }
    }

    @Override
    public Long pfadd(final byte[] key, final byte[]... elements) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PFADD)).time()) {
            return super.pfadd(key, elements);
        }
    }

    @Override
    public long pfcount(final byte[] key) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PFCOUNT)).time()) {
            return super.pfcount(key);
        }
    }

    @Override
    public List<byte[]> srandmember(final byte[] key, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SRANDMEMBER)).time()) {
            return super.srandmember(key, count);
        }
    }

    @Override
    public Long zlexcount(final byte[] key, final byte[] min, final byte[] max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZLEXCOUNT)).time()) {
            return super.zlexcount(key, min, max);
        }
    }

    @Override
    public Set<byte[]> zrangeByLex(final byte[] key, final byte[] min, final byte[] max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGEBYLEX)).time()) {
            return super.zrangeByLex(key, min, max);
        }
    }

    @Override
    public Set<byte[]> zrangeByLex(final byte[] key, final byte[] min, final byte[] max, final int offset, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZRANGEBYLEX)).time()) {
            return super.zrangeByLex(key, min, max, offset, count);
        }
    }

    @Override
    public Set<byte[]> zrevrangeByLex(final byte[] key, final byte[] max, final byte[] min) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYLEX)).time()) {
            return super.zrevrangeByLex(key, max, min);
        }
    }

    @Override
    public Set<byte[]> zrevrangeByLex(final byte[] key, final byte[] max, final byte[] min, final int offset, final int count) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREVRANGEBYLEX)).time()) {
            return super.zrevrangeByLex(key, max, min, offset, count);
        }
    }

    @Override
    public Long zremrangeByLex(final byte[] key, final byte[] min, final byte[] max) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZREMRANGEBYLEX)).time()) {
            return super.zremrangeByLex(key, min, max);
        }
    }

    @Override
    public Object eval(final byte[] script, final byte[] keyCount, final byte[]... params) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.EVAL)).time()) {
            return super.eval(script, keyCount, params);
        }
    }

    @Override
    public Object eval(final byte[] script, final int keyCount, final byte[]... params) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.EVAL)).time()) {
            return super.eval(script, keyCount, params);
        }
    }

    @Override
    public Object eval(final byte[] script, final List<byte[]> keys, final List<byte[]> args) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.EVAL)).time()) {
            return super.eval(script, keys, args);
        }
    }

    @Override
    public Object eval(final byte[] script, final byte[] sampleKey) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.EVAL)).time()) {
            return super.eval(script, sampleKey);
        }
    }

    @Override
    public Object evalsha(final byte[] sha1, final byte[] sampleKey) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.EVALSHA)).time()) {
            return super.evalsha(sha1, sampleKey);
        }
    }

    @Override
    public Object evalsha(final byte[] sha1, final List<byte[]> keys, final List<byte[]> args) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.EVALSHA)).time()) {
            return super.evalsha(sha1, keys, args);
        }
    }

    @Override
    public Object evalsha(final byte[] sha1, final int keyCount, final byte[]... params) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.EVALSHA)).time()) {
            return super.evalsha(sha1, keyCount, params);
        }
    }

    @Override
    public Long del(final byte[]... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.DEL)).time()) {
            return super.del(keys);
        }
    }

    @Override
    public List<byte[]> blpop(final int timeout, final byte[]... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.BLPOP)).time()) {
            return super.blpop(timeout, keys);
        }
    }

    @Override
    public List<byte[]> brpop(final int timeout, final byte[]... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.BRPOP)).time()) {
            return super.brpop(timeout, keys);
        }
    }

    @Override
    public List<byte[]> mget(final byte[]... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.MGET)).time()) {
            return super.mget(keys);
        }
    }

    @Override
    public String mset(final byte[]... keysvalues) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.MSET)).time()) {
            return super.mset(keysvalues);
        }
    }

    @Override
    public Long msetnx(final byte[]... keysvalues) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.MSETNX)).time()) {
            return super.msetnx(keysvalues);
        }
    }

    @Override
    public String rename(final byte[] oldkey, final byte[] newkey) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.RENAME)).time()) {
            return super.rename(oldkey, newkey);
        }
    }

    @Override
    public Long renamenx(final byte[] oldkey, final byte[] newkey) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.RENAMENX)).time()) {
            return super.renamenx(oldkey, newkey);
        }
    }

    @Override
    public byte[] rpoplpush(final byte[] srckey, final byte[] dstkey) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.RPOPLPUSH)).time()) {
            return super.rpoplpush(srckey, dstkey);
        }
    }

    @Override
    public Set<byte[]> sdiff(final byte[]... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SDIFF)).time()) {
            return super.sdiff(keys);
        }
    }

    @Override
    public Long sdiffstore(final byte[] dstkey, final byte[]... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SDIFFSTORE)).time()) {
            return super.sdiffstore(dstkey, keys);
        }
    }

    @Override
    public Set<byte[]> sinter(final byte[]... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SINTER)).time()) {
            return super.sinter(keys);
        }
    }

    @Override
    public Long sinterstore(final byte[] dstkey, final byte[]... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SINTERSTORE)).time()) {
            return super.sinterstore(dstkey, keys);
        }
    }

    @Override
    public Long smove(final byte[] srckey, final byte[] dstkey, final byte[] member) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SMOVE)).time()) {
            return super.smove(srckey, dstkey, member);
        }
    }

    @Override
    public Long sort(final byte[] key, final SortingParams sortingParameters, final byte[] dstkey) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SORT)).time()) {
            return super.sort(key, sortingParameters, dstkey);
        }
    }

    @Override
    public Long sort(final byte[] key, final byte[] dstkey) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SORT)).time()) {
            return super.sort(key, dstkey);
        }
    }

    @Override
    public Set<byte[]> sunion(final byte[]... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SUNION)).time()) {
            return super.sunion(keys);
        }
    }

    @Override
    public Long sunionstore(final byte[] dstkey, final byte[]... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SUNIONSTORE)).time()) {
            return super.sunionstore(dstkey, keys);
        }
    }

    @Override
    public Long zinterstore(final byte[] dstkey, final byte[]... sets) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZINTERSTORE)).time()) {
            return super.zinterstore(dstkey, sets);
        }
    }

    @Override
    public Long zinterstore(final byte[] dstkey, final ZParams params, final byte[]... sets) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZINTERSTORE)).time()) {
            return super.zinterstore(dstkey, params, sets);
        }
    }

    @Override
    public Long zunionstore(final byte[] dstkey, final byte[]... sets) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZUNIONSTORE)).time()) {
            return super.zunionstore(dstkey, sets);
        }
    }

    @Override
    public Long zunionstore(final byte[] dstkey, final ZParams params, final byte[]... sets) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZUNIONSTORE)).time()) {
            return super.zunionstore(dstkey, params, sets);
        }
    }

    @Override
    public byte[] brpoplpush(final byte[] source, final byte[] destination, final int timeout) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.BRPOPLPUSH)).time()) {
            return super.brpoplpush(source, destination, timeout);
        }
    }

    @Override
    public Long publish(final byte[] channel, final byte[] message) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PUBLISH)).time()) {
            return super.publish(channel, message);
        }
    }

    @Override
    public void subscribe(final BinaryJedisPubSub jedisPubSub, final byte[]... channels) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SUBSCRIBE)).time()) {
            super.subscribe(jedisPubSub, channels);
        }
    }

    @Override
    public void psubscribe(final BinaryJedisPubSub jedisPubSub, final byte[]... patterns) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PSUBSCRIBE)).time()) {
            super.psubscribe(jedisPubSub, patterns);
        }
    }

    @Override
    public Long bitop(final BitOP op, final byte[] destKey, final byte[]... srcKeys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.BITOP)).time()) {
            return super.bitop(op, destKey, srcKeys);
        }
    }

    @Override
    public String pfmerge(final byte[] destkey, final byte[]... sourcekeys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PFMERGE)).time()) {
            return super.pfmerge(destkey, sourcekeys);
        }
    }

    @Override
    public Long pfcount(final byte[]... keys) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.PFCOUNT)).time()) {
            return super.pfcount(keys);
        }
    }

    @Override
    public Long geoadd(final byte[] key, final double longitude, final double latitude, final byte[] member) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEOADD)).time()) {
            return super.geoadd(key, longitude, latitude, member);
        }
    }

    @Override
    public Long geoadd(final byte[] key, final Map<byte[], GeoCoordinate> memberCoordinateMap) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEOADD)).time()) {
            return super.geoadd(key, memberCoordinateMap);
        }
    }

    @Override
    public Double geodist(final byte[] key, final byte[] member1, final byte[] member2) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEODIST)).time()) {
            return super.geodist(key, member1, member2);
        }
    }

    @Override
    public Double geodist(final byte[] key, final byte[] member1, final byte[] member2, final GeoUnit unit) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEODIST)).time()) {
            return super.geodist(key, member1, member2, unit);
        }
    }

    @Override
    public List<byte[]> geohash(final byte[] key, final byte[]... members) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEOHASH)).time()) {
            return super.geohash(key, members);
        }
    }

    @Override
    public List<GeoCoordinate> geopos(final byte[] key, final byte[]... members) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEOPOS)).time()) {
            return super.geopos(key, members);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadius(final byte[] key, final double longitude, final double latitude, final double radius,
                                             final GeoUnit unit) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEORADIUS)).time()) {
            return super.georadius(key, longitude, latitude, radius, unit);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusReadonly(final byte[] key, final double longitude, final double latitude, final double radius,
                                                     final GeoUnit unit) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEORADIUS)).time()) {
            return super.georadiusReadonly(key, longitude, latitude, radius, unit);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadius(final byte[] key, final double longitude, final double latitude, final double radius,
                                             final GeoUnit unit, final GeoRadiusParam param) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEORADIUS)).time()) {
            return super.georadius(key, longitude, latitude, radius, unit, param);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusReadonly(final byte[] key, final double longitude, final double latitude, final double radius,
                                                     final GeoUnit unit, final GeoRadiusParam param) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEORADIUS)).time()) {
            return super.georadiusReadonly(key, longitude, latitude, radius, unit, param);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(final byte[] key, final byte[] member, final double radius, final GeoUnit unit) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEORADIUSBYMEMBER)).time()) {
            return super.georadiusByMember(key, member, radius, unit);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMemberReadonly(final byte[] key, final byte[] member, final double radius,
                                                             final GeoUnit unit) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEORADIUSBYMEMBER)).time()) {
            return super.georadiusByMemberReadonly(key, member, radius, unit);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(final byte[] key, final byte[] member, final double radius, final GeoUnit unit,
                                                     final GeoRadiusParam param) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEORADIUSBYMEMBER)).time()) {
            return super.georadiusByMember(key, member, radius, unit, param);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMemberReadonly(final byte[] key, final byte[] member, final double radius,
                                                             final GeoUnit unit, final GeoRadiusParam param) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.GEORADIUSBYMEMBER)).time()) {
            return super.georadiusByMemberReadonly(key, member, radius, unit, param);
        }
    }

    @Override
    public Set<byte[]> keys(final byte[] pattern) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.KEYS)).time()) {
            return super.keys(pattern);
        }
    }

    @Override
    public ScanResult<byte[]> scan(final byte[] cursor, final ScanParams params) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SCAN)).time()) {
            return super.scan(cursor, params);
        }
    }

    @Override
    public ScanResult<Map.Entry<byte[], byte[]>> hscan(final byte[] key, final byte[] cursor) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HSCAN)).time()) {
            return super.hscan(key, cursor);
        }
    }

    @Override
    public ScanResult<Map.Entry<byte[], byte[]>> hscan(final byte[] key, final byte[] cursor, final ScanParams params) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HSCAN)).time()) {
            return super.hscan(key, cursor, params);
        }
    }

    @Override
    public ScanResult<byte[]> sscan(final byte[] key, final byte[] cursor) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SSCAN)).time()) {
            return super.sscan(key, cursor);
        }
    }

    @Override
    public ScanResult<byte[]> sscan(final byte[] key, final byte[] cursor, final ScanParams params) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.SSCAN)).time()) {
            return super.sscan(key, cursor, params);
        }
    }

    @Override
    public ScanResult<Tuple> zscan(final byte[] key, final byte[] cursor) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZSCAN)).time()) {
            return super.zscan(key, cursor);
        }
    }

    @Override
    public ScanResult<Tuple> zscan(final byte[] key, final byte[] cursor, final ScanParams params) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.ZSCAN)).time()) {
            return super.zscan(key, cursor, params);
        }
    }

    @Override
    public List<Long> bitfield(final byte[] key, final byte[]... arguments) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.BITFIELD)).time()) {
            return super.bitfield(key, arguments);
        }
    }

    @Override
    public Long hstrlen(final byte[] key, final byte[] field) {
        try (final Timer.Context ignored = metrics.timer(MetricRegistry.name(name, RedisCommand.HSTRLEN)).time()) {
            return super.hstrlen(key, field);
        }
    }
}
