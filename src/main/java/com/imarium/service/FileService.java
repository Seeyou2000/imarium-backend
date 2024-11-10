package com.imarium.service;

import com.imarium.entity.Image;
import com.imarium.entity.Artwork;
import com.imarium.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ArtworkService artworkService; // Artwork 관련 서비스

    public List<String> saveFiles(List<MultipartFile> files, Long artworkId) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        // 해당 Artwork 찾기
        Artwork artwork = artworkService.getArtworkById(artworkId);

        // 각 파일을 업로드하고 이미지 URL을 저장
        for (MultipartFile file : files) {
            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.write(path, file.getBytes());

            // 이미지 엔티티 생성
            Image image = new Image();
            image.setImageUrl(path.toString()); // 파일 경로를 URL로 사용
            image.setArtwork(artwork); // 해당 Artwork에 이미지 연결

            fileRepository.save(image); // 이미지 저장

            imageUrls.add(path.toString()); // 저장된 이미지 URL 추가
        }

        return imageUrls; // 업로드된 이미지 URL 목록 반환
    }
}
