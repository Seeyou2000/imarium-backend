package com.imarium.service;

import com.imarium.entity.Col;
import com.imarium.repository.ColRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ColService {

    private final ColRepository columnRepository;

    public ColService(ColRepository columnRepository) {
        this.columnRepository = columnRepository;
    }

    public Col createColumn(Col column) {
        return columnRepository.save(column);
    }

    public List<Col> findByUserId(Long userId) {
        return columnRepository.findByUserId(userId);
    }

    public List<Col> findByIsRecommendedTrue() {
        return columnRepository.findByIsRecommendedTrue();
    }

    public Optional<Col> findById(Long columnId) {
        return columnRepository.findById(columnId);
    }

    public List<Col> findPopularOrLatestColumns(int limit) {
        // 예시: 최신순으로 정렬하여 제한된 수만큼 반환
        return columnRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream().limit(limit).collect(Collectors.toList());
    }
}

