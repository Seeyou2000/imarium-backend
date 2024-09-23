package com.imarium.service;

import com.imarium.entity.SearchHistory;
import com.imarium.repository.SearchHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;

    public SearchHistoryService(SearchHistoryRepository searchHistoryRepository) {
        this.searchHistoryRepository = searchHistoryRepository;
    }

    public List<SearchHistory> findByUserId(Long userId) {
        return searchHistoryRepository.findByUserId(userId);
    }

    public SearchHistory saveSearchHistory(SearchHistory searchHistory) {
        return searchHistoryRepository.save(searchHistory);
    }
}
