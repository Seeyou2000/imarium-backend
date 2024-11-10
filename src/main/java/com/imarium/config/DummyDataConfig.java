package com.imarium.config;

import com.imarium.entity.Artist;
import com.imarium.entity.Artwork;
import com.imarium.repository.ArtistRepository;
import com.imarium.repository.ArtworkRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DummyDataConfig {

    @Bean
    public CommandLineRunner insertDummyData(
            ArtistRepository artistRepository,
            ArtworkRepository artworkRepository) {
        return args -> {
            // Check if the artwork already exists
            if (artworkRepository.findByTitle("내가 죽으면 꼭 물로 돌려보내줘").isEmpty()) {
                // Assuming a user already exists or creating a new user
                Artist artist = artistRepository.findByName("김다희")
                        .orElseGet(() -> {
                            Artist newArtist = new Artist();
                            newArtist.setName("김다희");
                            newArtist.setEmail("kim@gmail.com");
                            newArtist.setPassword("1234");
                            newArtist.setIsArtist(true);
                            newArtist.setTags("아무거나");
                            newArtist.setLikes(0);
                            newArtist.setIsSavedLogin(true);
                            newArtist.setIsArtist(true);
                            newArtist.setBannerUrl("someBannerUrl");  // set actual banner URL if needed
                            artistRepository.save(newArtist);
                            return newArtist;
                        });

                Artwork artwork = new Artwork();
                artwork.setTitle("내가 죽으면 꼭 물로 돌려보내줘");
                artwork.setArtist(artist);
                artwork.setCategory("설치");
                artwork.setMaterial("메쉬 천, 패브릭에 사진 인쇄");
                artwork.setSize("2600x1000");
                artwork.setYear(2024);
                artwork.setIsSaved(true);
                artwork.setDescription("답답할 땐 수영을 합니다. 물에서는 중력조차 이겨내고 새처럼 날아다닐 수 있거든요. 이 해방감과 자유로움을 공유하고자 합니다.");
                artworkRepository.save(artwork);
            }
        };
    }
}
