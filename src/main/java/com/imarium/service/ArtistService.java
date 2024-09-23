package com.imarium.service;

import com.imarium.dto.ArtistInfoDto;
import com.imarium.dto.ArtworkDto;
import com.imarium.dto.ExhibitionDto;
import com.imarium.dto.PriceInfoDto;
import com.imarium.entity.*;
import com.imarium.repository.ArtistRepository;
import com.imarium.repository.ArtworkRepository;
import com.imarium.repository.ExhibitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtworkRepository artworkRepository;
    private final ExhibitionRepository exhibitionRepository;

    @Autowired
    public ArtistService(ArtistRepository artistRepository, ArtworkRepository artworkRepository, ExhibitionRepository exhibitionRepository) {
        this.artistRepository = artistRepository;
        this.artworkRepository = artworkRepository;
        this.exhibitionRepository = exhibitionRepository;
    }

    // 작가 Id 가져오기
    public Artist getArtistById(Long artistId) {
        return artistRepository.findById(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found with id: " + artistId));
    }

    // 작가 정보 가져오기
    public ArtistInfoDto getArtistInfo(Long artistId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found with id: " + artistId));

        // Artist는 User를 상속받으므로 artist.getName()을 통해 User의 name 필드를 가져옴
        return new ArtistInfoDto(
                artist.getId(),
                artist.getName(),  // 여기서 User의 name 필드를 참조
                artist.getTags(),
                artist.getLikes(),
                artist.getBannerUrl()
        );
    }

    // 작가의 작품 정보 가져오기
    public List<ArtworkDto> getArtistArtworks(Long artistId) {
        List<Artwork> artworks = artworkRepository.findByArtistId(artistId);
        return artworks.stream()
                .map(artwork -> new ArtworkDto(
                        artwork.getId(),
                        artwork.getTitle(),
                        artwork.getDescription(),
                        artwork.getMaterial(),
                        artwork.getSize(),
                        artwork.getYear(),
                        artwork.getIsSaved(),
                        artwork.getImages().stream().map(Image::getImageUrl).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    // 작가의 전시 정보 가져오기
    public List<ExhibitionDto> getArtistExhibitions(Long artistId) {
        List<Exhibition> exhibitions = exhibitionRepository.findByArtistId(artistId);
        return exhibitions.stream()
                .map(exhibition -> new ExhibitionDto(
                        exhibition.getId(),
                        exhibition.getTitle(),
                        exhibition.getDescription(),
                        exhibition.getIsSaved(),
                        exhibition.getImages().stream().map(Image::getImageUrl).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    // 작가의 작품 가격 정보 가져오기
    public PriceInfoDto getArtistArtworkPrices(Long artistId) {
        List<Artwork> artworks = artworkRepository.findByArtistId(artistId);

        if (artworks.isEmpty()) {
            return new PriceInfoDto(0, 0, 0);
        }

        int minPrice = artworks.stream()
                .flatMap(artwork -> artwork.getArtworkPrices().stream())  // ArtworkPrice에서 가격을 추출
                .mapToInt(ArtworkPrice::getMinPrice)
                .min()
                .orElse(0);

        int maxPrice = artworks.stream()
                .flatMap(artwork -> artwork.getArtworkPrices().stream())  // ArtworkPrice에서 가격을 추출
                .mapToInt(ArtworkPrice::getMaxPrice)
                .max()
                .orElse(0);

        int artworkCount = artworks.size();

        return new PriceInfoDto(maxPrice, minPrice, artworkCount);
    }
}


