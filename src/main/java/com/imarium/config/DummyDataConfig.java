package com.imarium.config;

import com.imarium.entity.*;
import com.imarium.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DummyDataConfig {

    @Bean
    public CommandLineRunner insertArtwork(
            ArtistRepository artistRepository,
            ArtworkRepository artworkRepository,
            ArtworkPriceRepository artworkPriceRepository,
            ReviewRepository reviewRepository,
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
                            newArtist.setBannerUrl("/uploads/artist.jpg");  // set actual banner URL if needed
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
                        "/uploads/artwork/image1.jpg",  // 실제 업로드된 이미지 URL을 사용
                        "/uploads/artwork/image2.jpg",
                        "/uploads/artwork/image3.jpg",
                        "/uploads/artwork/image4.jpg"
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

                List<String> contents = List.of(
                        "와 너무 멋있어요.",
                        "심오하네요...",
                        "이거 진짜에요?",
                        "실물로 봐야 더 좋은 작품이에요.",
                        "Wow, that's so cool."
                );

                for(String content : contents) {
                    Review review = new Review();
                    review.setUser(artist);
                    review.setArtworkId(artwork.getId());
                    review.setIsRecommended(true);
                    review.setLikes(0);
                    review.setContent(content);
                    reviewRepository.save(review);
                }
            }
        };
    }

    @Bean
    public CommandLineRunner insertCarousel(CarouselRepository carouselRepository){
        return args -> {
            Carousel carousel = new Carousel();
            carousel.setImageUrl("/uploads/carousel/image1.jpg");
            carousel.setLinkUrl("/artist/1");
            carouselRepository.save(carousel);
        };
    }

    @Bean
    public CommandLineRunner insertColumn(ColRepository colRepository){
        return args -> {
            // 이미지 URL 목록
            List<String> columnImageUrls = List.of(
                    "/uploads/column/image1.jpg",  // 실제 업로드된 이미지 URL을 사용
                    "/uploads/column/image2.jpg",
                    "/uploads/column/image3.jpg",
                    "/uploads/column/image4.jpg"
            );

            for(String columnImage : columnImageUrls) {
                Col col = new Col();
                col.setIsRecommended(true);
                col.setTitle("안녕하세요.");
                col.setImageUrl(columnImage);
                colRepository.save(col);
            }
        };
    }
}
