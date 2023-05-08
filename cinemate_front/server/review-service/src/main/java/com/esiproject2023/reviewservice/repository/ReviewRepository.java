package com.esiproject2023.reviewservice.repository;

import com.esiproject2023.reviewservice.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByContentId(String contentId);
}
