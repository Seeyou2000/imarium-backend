package com.imarium.service;

import com.imarium.entity.RelatedItem;
import com.imarium.entity.Tag;
import com.imarium.repository.RelatedItemRepository;
import com.imarium.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TagService {

    private final TagRepository tagRepository;
    private final RelatedItemRepository relatedItemRepository;

    public TagService(TagRepository tagRepository, RelatedItemRepository relatedItemRepository) {
        this.tagRepository = tagRepository;
        this.relatedItemRepository = relatedItemRepository;
    }

    public List<RelatedItem> findByTag(String tagName) {
        Tag tag = tagRepository.findByName(tagName)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found"));

        return relatedItemRepository.findByTagId(tag.getId());
    }

    // 기타 필요한 메서드
}

