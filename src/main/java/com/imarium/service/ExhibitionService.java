package com.imarium.service;

import com.imarium.entity.Exhibition;
import com.imarium.repository.ExhibitionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;

    public ExhibitionService(ExhibitionRepository exhibitionRepository) {
        this.exhibitionRepository = exhibitionRepository;
    }

    public Exhibition createExhibition(Exhibition exhibition) {
        return exhibitionRepository.save(exhibition);
    }

    public List<Exhibition> getExhibitionsByArtist(Long artistId) {
        return exhibitionRepository.findByArtistId(artistId);
    }

    public List<Exhibition> findAll() {
        return exhibitionRepository.findAll();
    }

    public Exhibition getExhibitionById(Long id) {
        return exhibitionRepository.findById(id).orElseThrow(() -> new RuntimeException("Exhibition not found"));
    }
}

