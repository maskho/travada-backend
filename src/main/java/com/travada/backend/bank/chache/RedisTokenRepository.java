package com.travada.backend.bank.chache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class RedisTokenRepository {

    final Logger logger = LoggerFactory.getLogger(RedisTokenRepository.class);
    private HashOperations hashOperations;

    public RedisTokenRepository(RedisTemplate redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void create(AuthToken authToken) {
        hashOperations.put("TOKEN", authToken.getTokenId(), authToken);
        logger.info(String.format("Token with ID %s saved", authToken.getTokenId()));
    }

    public AuthToken get(String tokenId) {
        return (AuthToken) hashOperations.get("TOKEN", tokenId);
    }

    public Map<String, AuthToken> getAll(){
        return hashOperations.entries("TOKEN");
    }

    public void update(AuthToken authToken) {
        hashOperations.put("TOKEN", authToken.getTokenId(), authToken);
        logger.info(String.format("Token with ID %s updated", authToken.getTokenId()));
    }

    public void delete(String tokenId) {
        hashOperations.delete("TOKEN", tokenId);
        logger.info(String.format("Token with ID %s deleted", tokenId));
    }

}
