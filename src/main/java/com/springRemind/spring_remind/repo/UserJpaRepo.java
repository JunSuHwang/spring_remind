package com.springRemind.spring_remind.repo;

import com.springRemind.spring_remind.entity.User;

import java.util.Optional;

public interface UserJpaRepo extends org.springframework.data.jpa.repository.JpaRepository<User, Long> {
    Optional<User> findByUid(String email);
    Optional<User> findByUidAndProvider(String uid, String provider);
}
