package com.esiproject2023.watchlistservice.controller;

import com.esiproject2023.watchlistservice.dto.MetadataResponse;
import com.esiproject2023.watchlistservice.dto.WatchlistItemDto;
import com.esiproject2023.watchlistservice.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/watchlist")
@RequiredArgsConstructor
public class WatchlistController {
    @Autowired
    private WatchlistService watchlistService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public WatchlistItemDto addContentToWatchlist(@RequestBody WatchlistItemDto watchlistItemDto) {
        return watchlistService.addWatchlist(watchlistItemDto);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<MetadataResponse> getUserWatchlist(@PathVariable Long userId) {
        return watchlistService.getWatchListForUser(userId);
    }

    @DeleteMapping("/{userId}/delete/{contentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFromWatchlist(@PathVariable Long userId, @PathVariable String contentId) {
        watchlistService.removeContentFromWatchlist(userId, contentId);
    }
}