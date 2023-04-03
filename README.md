# dropwizard-redis

Provides easy integration for Dropwizard applications with Redis via the Lettuce library.

This bundle comes with out-of-the-box support for:
* Configuration
* Client lifecycle management
* Client health checks
* Dropwizard Metrics integration
* Brave distributed tracing instrumentation integration for the Lettuce client.
* Support for the Lettuce cluster client.
* Support for the Lettuce sentinel client
* Support the the Lettuce basic Redis client

For more information on Redis, take a look at the official documentation here: https://redis.io/documentation

For More information on the Redis client used (Lettuce), see: https://github.com/lettuce-io/lettuce-core

## Dropwizard Version Support Matrix
| dropwizard-redis | Dropwizard v1.3.x  | Dropwizard v2.0.x  | Dropwizard v2.1.x  | Dropwizard v3.0.x  | Dropwizard v4.0.x  |
|------------------|--------------------|--------------------|--------------------|--------------------|--------------------|
| v1.3.x           | :white_check_mark: | :white_check_mark: | :x:                | :x:                | :x:                |
| v1.4.x           | :white_check_mark: | :white_check_mark: | :x:                | :x:                | :x:                |
| v1.5.x           | :white_check_mark: | :white_check_mark: | :x:                | :x:                | :x:                |
| v1.6.x           | :white_check_mark: | :white_check_mark: | :x:                | :x:                | :x:                |
| v1.7.x           | :white_check_mark: | :white_check_mark: | :question:         | :x:                | :x:                |
| v1.8.x           | :x:                | :question:         | :white_check_mark: | :x:                | :x:                |
| v3.0.x           | :x:                | :x:                | :x:                | :white_check_mark: | :x:                |
| v4.0.x           | :x:                | :x:                | :x:                | :x:                | :white_check_mark: |

## Usage
Add dependency on library.

Maven:
```xml
<dependency>
  <groupId>io.dropwizard.modules</groupId>
  <artifactId>dropwizard-redis</artifactId>
  <version>$latest-version</version>
</dependency>
```

Gradle:
```groovy
compile "io.dropwizard.modules:dropwizard-redis:$latest-version"
```


### Basic Lettuce Client
In your Dropwizard `Configuration` class, configure a `RedisClientFactory`:
```java
@Valid
@NotNull
@JsonProperty("redis")
private RedisClientFactory<String, String> redisClientFactory;
```

Then, in your `Application` class, you'll want to do something similar to the following:
```java
private final RedisClientBundle<String, String, ExampleConfiguration> redis = new RedisClientBundle<String, String, ExampleConfiguration>() {
    @Override
    public RedisClientFactory<String, String> getRedisClientFactory(ExampleConfiguration configuration) {
        return configuration.getRedisClientFactory();
    }
};

@Override
public void initialize(Bootstrap<ExampleConfiguration> bootstrap) {
    bootstrap.addBundle(redis);
}

@Override
public void run(ExampleConfiguration config, Environment environment) {
    final StatefulRedisConnection<String, String> connection = redisCluster.getConnection();
    final PersonCache personCache = new PersonCache(connection); // PersonCache is an arbtirary example
    environment.jersey().register(new PersonResource(personCache));
}
```


```yaml
redis:
  type: basic
  name: my-redis-use-case
  node:
    type: redis
    node: "127.0.0.1:6379"
    clientName: person-app
  redisCodec:
    type: string
  clientResources:
    type: default
    commandLatencyRecorder:
      type: default
      enabled: false
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
  name: my-redis-cluster-use-case
  nodes:
    - type: redis
      node: "127.0.0.1:6379"
      clientName: person-app
      password: hunter2
  redisCodec:
    type: string
  clientResources:
    type: default
    commandLatencyRecorder:
      type: default
      enabled: false
  # TODO: add more configs than just the required basics
```
