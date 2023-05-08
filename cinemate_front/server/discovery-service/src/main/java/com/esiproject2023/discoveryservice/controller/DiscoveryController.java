package com.esiproject2023.discoveryservice.controller;


import com.esiproject2023.discoveryservice.model.Content;
import com.esiproject2023.discoveryservice.service.DiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/discovery")
public class DiscoveryController {
    private final DiscoveryService discoveryService;

    @Autowired
    public DiscoveryController(DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    @GetMapping("/search/{params}") //search section
    public ResponseEntity<Content[]> getDiscoveryDataBySearch(@PathVariable String params) {
        try {
            Content[] content = discoveryService.processSearchResponse(params);
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{params}") //FOR YOU
    public ResponseEntity<Content[]> getDiscoveryData(@PathVariable String params) {
        try {
            Content[] top10Content = discoveryService.processDiscoveryResponse(params);
            return ResponseEntity.ok(top10Content);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
