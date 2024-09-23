package com.imarium.service;

import com.imarium.entity.Carousel;
import com.imarium.repository.CarouselRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CarouselService {

    private final CarouselRepository carouselRepository;

    public CarouselService(CarouselRepository carouselRepository) {
        this.carouselRepository = carouselRepository;
    }

    public List<Carousel> findAll() {
        return carouselRepository.findAll();
    }

    public Carousel saveCarousel(Carousel carousel) {
        return carouselRepository.save(carousel);
    }
}
