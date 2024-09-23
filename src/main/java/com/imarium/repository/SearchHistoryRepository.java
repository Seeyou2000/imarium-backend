package com.imarium.repository;

import com.imarium.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    // 특정 사용자의 검색 기록 찾기
    List<SearchHistory> findByUserId(Long userId);
}

