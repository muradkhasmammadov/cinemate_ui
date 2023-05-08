package com.esiproject2023.reviewservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MetadataResponse {
    private String id;
    private String title;
    private double rating;
    private String cast;
    private String description;
    private String genre;
    private String poster;
    private String release_date;
    private String director;
}
