package com.imarium.repository;

import com.imarium.entity.Artist;
import com.imarium.entity.Artwork;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    // 작가 이름으로 작가 찾기
    Optional<Artist> findByName(String name);
}
