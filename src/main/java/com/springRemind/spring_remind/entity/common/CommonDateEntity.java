package com.springRemind.spring_remind.entity.common;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
// 공통 매핑 정보를 상속해서 쓰고 싶을때 사용(DB 구조 변경 x)
@MappedSuperclass
// 미리 정의된 엔티티 리스너를 사용
@EntityListeners(AuditingEntityListener.class)
public abstract class CommonDateEntity {
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
