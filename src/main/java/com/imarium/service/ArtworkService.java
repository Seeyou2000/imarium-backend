package com.imarium.service;
import com.imarium.entity.Artwork;
import com.imarium.repository.ArtworkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ArtworkService {

    private final ArtworkRepository artworkRepository;

    public ArtworkService(ArtworkRepository artworkRepository) {
        this.artworkRepository = artworkRepository;
    }

    public Artwork createArtwork(Artwork artwork) {
        return artworkRepository.save(artwork);
    }

    public List<Artwork> getArtworksByArtist(Long artistId) {
        return artworkRepository.findByArtistId(artistId);
    }

    public List<Artwork> findAll() {
        return artworkRepository.findAll();
    }

    public Artwork getArtworkById(Long id) {
        return artworkRepository.findById(id).orElseThrow(() -> new RuntimeException("Artwork not found"));
    }
}

