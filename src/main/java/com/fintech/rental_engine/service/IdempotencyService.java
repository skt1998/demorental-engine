package com.fintech.rental_engine.service;

import com.fintech.rental_engine.entity.IdempotencyRecord;
import com.fintech.rental_engine.repository.IdempotencyRecordRepository;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class IdempotencyService {

    private final RedissonClient redissonClient;
    private final IdempotencyRecordRepository repository;

    public IdempotencyService(RedissonClient redissonClient, IdempotencyRecordRepository repository) {
        this.redissonClient = redissonClient;
        this.repository = repository;
    }

    public boolean lockKey(String key) {
        String redisKey = "idempotency:" + key;
        RBucket<String> bucket = redissonClient.getBucket(redisKey);
        return bucket.setIfAbsent("IN_PROGRESS", Duration.ofMinutes(10));
    }

    public Optional<IdempotencyRecord> getRecord(String key) {
        return repository.findById(key);
    }

    public void saveRecord(String key, String path, int status, String body) {
        IdempotencyRecord record = new IdempotencyRecord(key, path, status, body);
        repository.save(record);

        String redisKey = "idempotency:" + key;
        RBucket<String> bucket = redissonClient.getBucket(redisKey);
        bucket.set("COMPLETED", Duration.ofHours(24));
    }
}