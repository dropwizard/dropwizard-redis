# dropwizard-redis

Provides easy integration for Dropwizard applications with Redis via the Lettuce library.

This bundle comes with out-of-the-box support for:
* Configuration
* Client lifecycle management
* Client health checks
* Dropwizard Metrics integration

For more information on Redis, take a look at the official documentation here: https://redis.io/documentation

For More information on the Redis client used (Lettuce), see: https://github.com/lettuce-io/lettuce-core

## Future Improvements
* Distributed tracing instrumentation integration for the Lettuce client.
* Support for the Lettuce cluster client.
* Support for the Lettuce sentinel client
* Support the the Lettuce basic Redis client 

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

### Lettuce Cluster Client
In your Dropwizard `Configuration` class, configure a `RedisClusterClientFactory`:
```java
@Valid
@NotNull
@JsonProperty("redis-cluster")
private RedisClusterClientFactory<String, String> redisClientFactory;
```

Then, in your `Application` class, you'll want to do something similar to the following:
```java
private final RedisClusterClientBundle<String, String, ExampleConfiguration> redisCluster = new RedisClusterClientBundle<String, String, ExampleConfiguration>() {
    @Override
    public RedisClusterClientFactory<String, String> getRedisClusterClientFactory(ExampleConfiguration configuration) {
        return configuration.getRedisClusterClientFactory();
    }
};

@Override
public void initialize(Bootstrap<ExampleConfiguration> bootstrap) {
    bootstrap.addBundle(redisCluster);
}

@Override
public void run(ExampleConfiguration config, Environment environment) {
    final StatefulRedisClusterConnection<String, String> clusterConnection = redisCluster.getClusterConnection();
    final PersonCache personCache = new PersonCache(clusterConnection); // PersonCache is an arbtirary example
    environment.jersey().register(new PersonResource(personCache));
}
```

Configure your factory in your `config.yml` file:
```yaml
redis-cluster:
  type: cluster
  name: my-redis-use-case
  nodes:
    - type: redis
      node: "127.0.0.1:6379"
      clientName: person-app
      password: hunter2
  redisCodec:
    type: string
  # TODO: add more configs than just the required basics
```
