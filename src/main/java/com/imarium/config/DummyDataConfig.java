package com.imarium.config;

import com.imarium.entity.Artist;
import com.imarium.entity.Artwork;
import com.imarium.entity.ArtworkPrice;
import com.imarium.entity.Image;
import com.imarium.repository.ArtistRepository;
import com.imarium.repository.ArtworkPriceRepository;
import com.imarium.repository.ArtworkRepository;
import com.imarium.repository.FileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DummyDataConfig {

    @Bean
    public CommandLineRunner insertDummyData(
            ArtistRepository artistRepository,
            ArtworkRepository artworkRepository,
            ArtworkPriceRepository artworkPriceRepository,
            FileRepository fileRepository) {
        return args -> {
            // Check if the artwork already exists
            if (artworkRepository.findByTitle("내가 죽으면 꼭 물로 돌려보내줘").isEmpty()) {
                // Assuming an artist already exists or creating a new artist
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

                // 이미지 URL 목록
                List<String> imageUrls = List.of(
                        "/uploads/image1.jpg",  // 실제 업로드된 이미지 URL을 사용
                        "/uploads/image2.jpg",
                        "/uploads/image3.jpg",
                        "/uploads/image4.jpg"
                );

                // 이미지 추가
                for (String url : imageUrls) {
                    Image image = new Image();
                    image.setImageUrl(url);
                    image.setArtwork(artwork);  // 해당 Artwork과 연결
                    fileRepository.save(image);  // 이미지 저장
                    artwork.getImages().add(image);  // Artwork 객체의 images 리스트에 이미지 추가
                }

                // Artwork의 가격 설정
                ArtworkPrice artworkPrice = new ArtworkPrice();
                artworkPrice.setArtwork(artwork);
                artworkPrice.setMinPrice(600000);
                artworkPrice.setMaxPrice(600000);
                artworkPriceRepository.save(artworkPrice);
            }
        };
    }
}
