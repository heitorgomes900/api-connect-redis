package com.github.heitorgomes900.apiConnectRedis.service;

import io.lettuce.core.RedisClient;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RedisService {

    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;
    private RedisCommands<String, String> syncCommands;

    @PostConstruct
    public void init() {
        redisClient = RedisClient.create("redis://localhost:6379");
        connection = redisClient.connect();
        syncCommands = connection.sync();
    }

    @PreDestroy
    public void close() {
        connection.close();
        redisClient.shutdown();
    }

    public List<String> findKeysByPattern(String pattern) {
        List<String> keys = new ArrayList<>();
        ScanArgs scanArgs = ScanArgs.Builder.matches("*" + pattern + "*");
        ScanCursor cursor = ScanCursor.INITIAL;

        do {
            var scanResult = syncCommands.scan(cursor, scanArgs);
            cursor = scanResult;
            scanResult.getKeys().forEach(keys::add);
        } while (!cursor.isFinished());

        return keys;
    }

    public String getObjectByKey(String key) {
        return syncCommands.get(key);
    }

    public List<String> getObjectsByKeyPattern(String pattern) {
        List<String> keys = findKeysByPattern(pattern);
        return keys.stream().map(this::getObjectByKey).toList();
    }
}
