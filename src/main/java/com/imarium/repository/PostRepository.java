package com.imarium.repository;

import com.imarium.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 필요한 추가 메서드 정의
}
