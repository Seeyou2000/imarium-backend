package com.imarium.service;

import com.imarium.entity.ArtworkPrice;
import com.imarium.repository.ArtworkPriceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ArtworkPriceService {

    private final ArtworkPriceRepository artworkPriceRepository;

    public ArtworkPriceService(ArtworkPriceRepository artworkPriceRepository) {
        this.artworkPriceRepository = artworkPriceRepository;
    }

    public List<ArtworkPrice> getPricesByArtwork(Long artworkId) {
        return artworkPriceRepository.findByArtworkId(artworkId);
    }
}

