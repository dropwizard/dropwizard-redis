# dropwizard-redis

Provides easy integration for Dropwizard applications with Redis.

This bundle comes with out-of-the-box support for:
* Configuration
* Clientlifecycle management
* Client health checks
* Metrics integration

For more information on Redis, take a look at the official documentation here: https://redis.io/documentation

For More information on the Redis client used (Jedis), see: https://github.com/xetorthio/jedis

## Future Improvements
* Distributed tracing instrumentation for Jedis.
* Support for the pooled Jedis client
* Support for the sentinel Jedis client
* SSL support for the cluster Jedis client (once Jedis supports this) 

## Usage
Add dependency on library.

Maven:
```
<dependency>
  <groupId>io.dropwizard.modules</groupId>
  <artifactId>dropwizard-redis</artifactId>
  <version>1.3.12-1</version>
</dependency>
```

Gradle:
```
compile "io.dropwizard.modules:dropwizard-redis:1.3.12-1"
```

In your Dropwizard configuration class, configure a `RedisClusterClientFactory`
```
@Valid
@NotNull
@JsonProperty
private RedisClusterClientFactory redis;
```

### Jedis Cluster Client
In your Dropwizard `Configuration` class, configure a `RedisClusterClientFactory`:
```java
@Valid
@NotNull
@JsonProperty("redis-cluster")
private RedisClusterClientFactory redisClusterClientFactory;
```

Then, in your `Application` class, you'll want to do something similar to the following:
```java
private final RedisClusterClientBundle<ExampleConfiguration> redisCluster = new RedisClusterClientBundle<ExampleConfiguration>() {
    @Override
    public RedisClusterClientFactory getRedisClusterClientFactory(ExampleConfiguration configuration) {
        return configuration.getRedisClusterClientFactory();
    }
};

@Override
public void initialize(Bootstrap<ExampleConfiguration> bootstrap) {
    bootstrap.addBundle(redisCluster);
}

@Override
public void run(ExampleConfiguration config, Environment environment) {
    final PersonCache personCache = new personCache(kafkaProducer.getClusterClient());
    environment.jersey().register(new PersonResource(personCache));
}
```

Configure your factory in your `config.yml` file:
```
type: jedis
name: dev
metricsEnabled: true # default
pool:
  maxTotalConnections: 8 # default
  minIdleConnections: 0 # default
  maxIdleConnections: 8 # default
  jmxEnabled: true # default
  jmxNameBase: some-base-name
  jmxNamePrefix: some-prefix
  blockWhenExhausted: true # default
  timeBetweenEvictionRuns: 10s
  testWhileIdle: false # default
  testOnReturn: false # default
  testOnBorrow: false # default
  testOnCreate: false # default
  minEvictableIdleTime: 30s # default
  softMinEvictableIdleTime: 15s
  maxWait: 5s
  testsPerEvictionRun: 3 # default
  evictionPolicy: "org.apache.commons.pool2.impl.DefaultEvictionPolicy" # default
  evictorShutdownTimeout: 10s # default
  fairness: false # default
  lifo: true # default
connectionTimeout: 2s # default
socketTimeout: 2s # default
maxAttempts: 5 # default
password: hunter2
nodes:
- host: localhost
  port: 6371
```
