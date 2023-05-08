package com.esiproject2023.metadataservice.model;


public record Metadata(String id, String title, double rating, String cast, String description, String genre, String poster, String release_date, String director) {
}
