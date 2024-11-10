package com.imarium.repository;

import com.imarium.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<Image, Long> {

}
