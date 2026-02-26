package com.rentpeeasy.backend.service;

import com.rentpeeasy.backend.dto.PropertyDto;
import com.rentpeeasy.backend.entity.Favorite;
import com.rentpeeasy.backend.entity.Property;
import com.rentpeeasy.backend.entity.User;
import com.rentpeeasy.backend.exception.ResourceNotFoundException;
import com.rentpeeasy.backend.repository.FavoriteRepository;
import com.rentpeeasy.backend.repository.PropertyRepository;
import com.rentpeeasy.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    
    public List<PropertyDto> getUserFavorites(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return favoriteRepository.findByUserId(user.getId()).stream()
                .map(favorite -> mapPropertyToDto(favorite.getProperty()))
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void addFavorite(UUID propertyId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));
        
        // Check if already favorited
        if (favoriteRepository.existsByUserIdAndPropertyId(user.getId(), propertyId)) {
            throw new IllegalArgumentException("Property already in favorites");
        }
        
        Favorite favorite = Favorite.builder()
                .user(user)
                .property(property)
                .build();
        
        favoriteRepository.save(favorite);
    }
    
    @Transactional
    public void removeFavorite(UUID propertyId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Favorite favorite = favoriteRepository.findByUserIdAndPropertyId(user.getId(), propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found"));
        
        favoriteRepository.delete(favorite);
    }
    
    private PropertyDto mapPropertyToDto(Property property) {
        return PropertyDto.builder()
                .id(property.getId())
                .title(property.getTitle())
                .description(property.getDescription())
                .type(property.getType())
                .city(property.getCity())
                .locality(property.getLocality())
                .price(property.getPrice())
                .priceUnit(property.getPriceUnit())
                .beds(property.getBeds())
                .baths(property.getBaths())
                .squareFeet(property.getSquareFeet())
                .isFeatured(property.getIsFeatured())
                .isVerified(property.getIsVerified())
                .images(property.getImages())
                .amenities(property.getAmenities())
                .ownerId(property.getOwner().getId())
                .ownerName(property.getOwner().getFullName())
                .contactNumber(property.getContactNumber())
                .status(property.getStatus())
                .createdAt(property.getCreatedAt())
                .updatedAt(property.getUpdatedAt())
                .build();
    }
}