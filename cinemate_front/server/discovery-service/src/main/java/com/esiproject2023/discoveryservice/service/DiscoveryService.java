package com.esiproject2023.discoveryservice.service;

import com.esiproject2023.discoveryservice.model.Content;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DiscoveryService {
    @Autowired
    private WebClient.Builder webClient;

    public Content[] processSearchResponse(String params) throws JsonProcessingException {
        String[] allParams = params.split(",");
        String movieTitle = allParams[0];
        String titleType = allParams[1];
        String genre = allParams[2];
        String list = allParams[3];
        String sort = allParams[4];
        String year = allParams[5];
        String endYear = allParams[6];
        String startYear = allParams[7];
        String page = allParams[8];

        String processedSearch = ("AKA" + movieTitle + "?exact=false&" + createConfig(titleType, genre, list, sort, year, endYear, startYear, page,0));

        String response = webClient.build().get().uri("http://metadata-service/metadata/searchByParams/{params}", processedSearch).retrieve().bodyToMono(String.class).block();

        Gson gson = new Gson();
        return gson.fromJson(response, Content[].class);
    }

    public Content[] processDiscoveryResponse(String params) throws JsonProcessingException {
        String[] allParams = params.split(",");
        int userAge = ageCalculator(allParams[0]);
        String favGenre = allParams[1];
        double ratingLimit = Double.parseDouble(allParams[2]);

        String top_rated_250 = createConfig("", favGenre, "top_rated_250", "", "", "", "", "",30);
        String top_rated_series_250 = createConfig("", favGenre, "top_rated_series_250", "", "", "", "", "",30);

        String response1 = webClient.build().get().uri("http://metadata-service/metadata/searchByParams/{params}", top_rated_250).retrieve().bodyToMono(String.class).block();
        String response2 = webClient.build().get().uri("http://metadata-service/metadata/searchByParams/{params}", top_rated_series_250).retrieve().bodyToMono(String.class).block();

        Gson gson = new Gson();
        List<Content> content1 = Arrays.asList(gson.fromJson(response1, Content[].class));
        List<Content> content2 = Arrays.asList(gson.fromJson(response2, Content[].class));


        List<Content> contents = new ArrayList<>(content1);
        contents.addAll(content2);
        List<Content> filteredContents = new ArrayList<>(contents.stream()
                .filter(content -> isContentValid(content, userAge, ratingLimit)).toList());

        // Sort the contents by rating
        filteredContents.sort((c1, c2) -> (int) (c2.getRating() * 100 - c1.getRating() * 100));

        // Get the top 10 contents
        List<Content> top10Contents = new ArrayList<>();
        for (int i = 0; i < filteredContents.size() && i < 10; i++) {
            top10Contents.add(filteredContents.get(i));
        }
        return top10Contents.toArray(new Content[0]);
    }

    private int ageCalculator(String dateOfBirth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dob = LocalDate.parse(dateOfBirth, formatter);

        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(dob, currentDate);
        return age.getYears();
    }

    private boolean isContentValid(Content c, int userAge, double ratingLimit) {
        boolean valid = true;
        List<String> genresList = Arrays.asList(c.getGenre().split(","));
        List<String> checkList = Arrays.asList("musicVideo", "podcastEpisode", "podcastSeries", "videoGame", "video");

        if (c.containsNull()) return false;

        for (String genre : genresList) {
            if (checkList.contains(genre)) {
                valid = false;
                break;
            }
        }
        return valid && c.getRating() > ratingLimit && (userAge >= 18 || !genresList.contains("Romance"));
    }


    private String createConfig(String titleType, String genre, String list, String sort, String year, String endYear, String startYear, String page, int limit) {
        LinkedHashMap<String, String> parameterTypes = new LinkedHashMap<String, String>();
        parameterTypes.put("titleType", titleType.equals("no") ? "" : titleType);
        parameterTypes.put("genre", genre.equals("no")? "" : genre);
        parameterTypes.put("list", list.equals("no")? "" : list);
        parameterTypes.put("sort", sort.equals("no")? "" : sort);
        parameterTypes.put("limit", limit != 0 ?  Integer.toString(limit) : "");
        parameterTypes.put("endYear", endYear.equals("no")? "" : endYear);
        parameterTypes.put("year", year.equals("no")? "" : year);
        parameterTypes.put("info", "custom_info");
        parameterTypes.put("page", page.equals("no")? "" : page);
        parameterTypes.put("startYear", startYear.equals("no")? "" : startYear);

        StringBuilder queryStringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : parameterTypes.entrySet()) {
            if (!entry.getValue().equals("")) {
                if (queryStringBuilder.length() > 0) {
                    queryStringBuilder.append("&");
                }
                queryStringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        return queryStringBuilder.toString();
    }
}
