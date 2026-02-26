package com.rentpeeasy.backend.controller;

import com.rentpeeasy.backend.dto.PropertyDto;
import com.rentpeeasy.backend.dto.PropertyRequest;
import com.rentpeeasy.backend.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {
    
    private final PropertyService propertyService;
    
    @GetMapping
    public ResponseEntity<List<PropertyDto>> getAllProperties(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice
    ) {
        if (city != null || type != null || minPrice != null || maxPrice != null) {
            return ResponseEntity.ok(propertyService.searchProperties(city, type, minPrice, maxPrice));
        }
        return ResponseEntity.ok(propertyService.getAllProperties());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PropertyDto> getPropertyById(@PathVariable UUID id) {
        return ResponseEntity.ok(propertyService.getPropertyById(id));
    }
    
    @GetMapping("/featured")
    public ResponseEntity<List<PropertyDto>> getFeaturedProperties() {
        return ResponseEntity.ok(propertyService.getFeaturedProperties());
    }
    
    @PostMapping
    public ResponseEntity<PropertyDto> createProperty(
            @Valid @RequestBody PropertyRequest request,
            Authentication authentication
    ) {
        PropertyDto property = propertyService.createProperty(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(property);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PropertyDto> updateProperty(
            @PathVariable UUID id,
            @Valid @RequestBody PropertyRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(propertyService.updateProperty(id, request, authentication.getName()));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProperty(
            @PathVariable UUID id,
            Authentication authentication
    ) {
        propertyService.deleteProperty(id, authentication.getName());
        return ResponseEntity.ok("Property deleted successfully");
    }
}