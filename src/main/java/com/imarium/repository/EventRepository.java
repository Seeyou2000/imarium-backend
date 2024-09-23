package com.imarium.repository;

import com.imarium.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    // 필요한 추가 메서드 정의
}

