package com.esiproject2023.watchlistservice.repository;

import com.esiproject2023.watchlistservice.model.WatchlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchlistRepository extends JpaRepository<WatchlistItem, Long> {
    List<WatchlistItem> findByUserId(Long userId);
    WatchlistItem findByUserIdAndContentId(Long userId, String contentId);
}
