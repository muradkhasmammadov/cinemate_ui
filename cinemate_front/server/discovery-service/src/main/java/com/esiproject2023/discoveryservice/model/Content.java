package com.esiproject2023.discoveryservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Content {
    private String id;
    private String title;
    private double rating;
    private String cast;
    private String description;
    private String genre;
    private String poster;
    private String release_date;
    private String director;

    public boolean containsNull() {
        return id.equals("not found") || title.equals("not found") || rating == 0 || cast.equals("not found")
                || description.equals("not found") || genre.equals("not found") || poster.equals("not found")
                || release_date.equals("not found") || director.equals("not found");
    }
}
