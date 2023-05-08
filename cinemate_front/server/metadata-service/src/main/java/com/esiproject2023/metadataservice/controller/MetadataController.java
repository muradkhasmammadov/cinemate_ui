package com.esiproject2023.metadataservice.controller;

import com.esiproject2023.metadataservice.model.Metadata;
import com.esiproject2023.metadataservice.service.MetadataService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/metadata")
public class MetadataController {
    private final MetadataService dataService;

    @Autowired
    public MetadataController(MetadataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/search")
    public ResponseEntity<Metadata[]> getAllMetadata() {
        try {
            String response = dataService.getResponse();
            Metadata[] allMetadata = dataService.processResponse(response);
            return ResponseEntity.ok(allMetadata);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/searchByParams/{params}")
    public ResponseEntity<Metadata[]> getRequiredMetadata(@PathVariable String params) {
        try {
            String response = dataService.getResponseWithParams(params);
            Metadata[] allMetadata = dataService.processResponse(response);
            return ResponseEntity.ok(allMetadata);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/searchByIDs/{ids}")
    public ResponseEntity<Metadata[]> getAllMetadataByIDs(@PathVariable String ids) {
        try {
            String response = dataService.getResponseWithIDs(ids);
            Metadata[] allMetadata = dataService.processResponse(response);
            return ResponseEntity.ok(allMetadata);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/searchByID/{id}")
    public ResponseEntity<String> getMetadataAndRatingByID(@PathVariable String id) {
        try {
            Gson gson = new Gson();
            String metadata = dataService.getResponseWithIDs(id);
            String reviews = dataService.getReviewByID(id);
            Metadata allMetadata = dataService.processResponse(metadata)[0];

            return ResponseEntity.ok(gson.toJson(allMetadata + reviews));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
