package com.esiproject2023.reviewservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="review")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "contentId", nullable = false)
    private String contentId;

    @Column(name = "body", nullable = true)
    private String body;

    @Column(name = "score", nullable = false)
    private Double score;
}
