package com.springRemind.spring_remind.service.cache;

import com.springRemind.spring_remind.common.CacheKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CacheService {

    @Caching(evict = {
            @CacheEvict(value = CacheKey.POST, key = "#p0"),
            @CacheEvict(value = CacheKey.POSTS, key = "#p1")
    })
    public boolean deleteBoardCache(long postId, String boardName) {
        log.debug("deleteBoardCache - postId {}, boardName {}", postId, boardName);
        return true;
    }
}
