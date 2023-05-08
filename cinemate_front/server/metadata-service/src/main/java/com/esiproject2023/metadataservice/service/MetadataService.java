package com.esiproject2023.metadataservice.service;

import com.esiproject2023.metadataservice.model.Metadata;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MetadataService {
    @Value("${app.API_URL}")
    private String API_URL;
    @Value("${app.ContentType}")
    private String contentType;
    @Value("${app.API_Key}")
    private String API_Key;
    @Value("${app.API_Host}")
    private String API_Host;

    public String getResponseWithParams(String params) throws Exception {
        StringBuilder restUrl = new StringBuilder();
        restUrl.append(API_URL);
        if (params.contains("AKA")) {
            restUrl.append("/search/title/");
            restUrl.append(params.substring(3));

        } else restUrl.append("?").append(params);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(restUrl.toString()))
                .header("content-type", contentType)
                .header("X-RapidAPI-Key", API_Key)
                .header("X-RapidAPI-Host", API_Host)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String getResponseWithIDs(String ids) throws Exception {
        StringBuilder params = new StringBuilder();
        params.append("/x/titles-by-ids?idsList=");
        for (String ID : ids.split(",")) {
            if (!ID.equals("")) {
                if (params.length() > 0) {
                    params.append("%2C");
                }
                params.append(ID);
            }
        }
        params.append("&info=custom_info");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + params))
                .header("content-type", contentType)
                .header("X-RapidAPI-Key", API_Key)
                .header("X-RapidAPI-Host", API_Host)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String getReviewByID(String ids) throws Exception {
        StringBuilder params = new StringBuilder();
        params.append("/x/titles-by-ids?idsList=");
        for (String ID : ids.split(",")) {
            if (!ID.equals("")) {
                if (params.length() > 0) {
                    params.append("%2C");
                }
                params.append(ID);
            }
        }
        params.append("&info=custom_info");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + params))
                .header("content-type", contentType)
                .header("X-RapidAPI-Key", API_Key)
                .header("X-RapidAPI-Host", API_Host)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String getResponse() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "?info=custom_info"))
                .header("content-type", contentType)
                .header("X-RapidAPI-Key", API_Key)
                .header("X-RapidAPI-Host", API_Host)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public Metadata[] processResponse(String response) {
        if (!response.equals("") && !response.equals("{}")) {
            JsonArray results = JsonParser.parseString(response).getAsJsonObject().getAsJsonArray("results");
            List<Metadata> metadataList = new ArrayList<>();
            for (int i = 0; i < results.size(); i++) {
                JsonObject result = results.get(i).getAsJsonObject();
                String id = result.get("id").getAsString();
                boolean titleTextExist = !result.getAsJsonObject("titleText").get("text").isJsonNull();
                String titleText = titleTextExist ? result.getAsJsonObject("titleText").get("text").getAsString() : "";
                boolean ratingCheck = result.getAsJsonObject("ratingsSummary")
                        .get("aggregateRating")
                        .isJsonNull();

                double rating = ratingCheck ? 0 : result.getAsJsonObject("ratingsSummary")
                        .get("aggregateRating")
                        .getAsDouble();

                boolean plotText = !(result.get("plot").isJsonNull()) && (!result.getAsJsonObject("plot").get("plotText").isJsonNull());
                String description = plotText
                        ?
                        result.getAsJsonObject("plot").getAsJsonObject("plotText").get("plainText").getAsString()
                        : "";

                boolean castExist = result.get("principalCast") != null && (result.getAsJsonArray("principalCast").size()) > 0;
                JsonArray casts = castExist ? result.getAsJsonArray("principalCast").get(0).getAsJsonObject().getAsJsonArray("credits") : new JsonArray();
                StringBuilder allCasts = new StringBuilder();

                for (int itr = 0; itr < casts.size(); itr++) {
                    if (itr > 0) {
                        allCasts.append(", ");
                    }
                    String name = casts.get(itr).getAsJsonObject().getAsJsonObject("name").getAsJsonObject("nameText").get("text").getAsString();
                    allCasts.append(name);
                }

                boolean genresExist = !result.get("genres").isJsonNull();
                JsonArray genres = genresExist ? result.getAsJsonObject("genres").getAsJsonArray("genres") : new JsonArray();
                StringBuilder allGenres = new StringBuilder();
                for (int itr = 0; itr < genres.size(); itr++) {
                    if (itr > 0) {
                        allGenres.append(", ");
                    }
                    allGenres.append(genres.get(itr).getAsJsonObject().get("text").getAsString());
                }

                boolean imageExist = !result.get("primaryImage").isJsonNull() && !result.getAsJsonObject("primaryImage").get("url").isJsonNull();
                String imageURL = imageExist ? result.getAsJsonObject("primaryImage").get("url").getAsString() : "not found";

                boolean dateExist = !result.get("releaseDate").isJsonNull();
                String releaseDate;
                if (dateExist) {
                    JsonObject date = result.getAsJsonObject("releaseDate");
                    releaseDate = date.get("day") + "-" + date.get("month") + "-" + date.get("year");
                } else {
                    releaseDate = "not found";
                }

                boolean directorsExist = (result.getAsJsonArray("directors").size()) > 0;
                String director = directorsExist ? result.getAsJsonArray("directors").get(0).getAsJsonObject().getAsJsonArray("credits").get(0).getAsJsonObject().getAsJsonObject("name").getAsJsonObject("nameText").get("text").getAsString() : "not found";

                Metadata metadata = new Metadata(id, titleText, rating, allCasts.toString(), description, allGenres.toString(), imageURL, releaseDate, director); //, genre, releaseDate, organization, awards.toArray(new String[0])
                metadataList.add(metadata);
            }
            return metadataList.toArray(new Metadata[0]);
        } else {
            return new Metadata[0];
        }
    }

}
