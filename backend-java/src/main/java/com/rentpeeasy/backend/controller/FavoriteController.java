package com.rentpeeasy.backend.controller;

import com.rentpeeasy.backend.dto.PropertyDto;
import com.rentpeeasy.backend.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    
    private final FavoriteService favoriteService;
    
    @GetMapping
    public ResponseEntity<List<PropertyDto>> getUserFavorites(Authentication authentication) {
        return ResponseEntity.ok(favoriteService.getUserFavorites(authentication.getName()));
    }
    
    @PostMapping("/{propertyId}")
    public ResponseEntity<String> addFavorite(
            @PathVariable UUID propertyId,
            Authentication authentication
    ) {
        favoriteService.addFavorite(propertyId, authentication.getName());
        return ResponseEntity.ok("Property added to favorites");
    }
    
    @DeleteMapping("/{propertyId}")
    public ResponseEntity<String> removeFavorite(
            @PathVariable UUID propertyId,
            Authentication authentication
    ) {
        favoriteService.removeFavorite(propertyId, authentication.getName());
        return ResponseEntity.ok("Property removed from favorites");
    }
}