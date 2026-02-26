package com.rentpeeasy.backend.service;

import com.rentpeeasy.backend.dto.PropertyDto;
import com.rentpeeasy.backend.dto.PropertyRequest;
import com.rentpeeasy.backend.entity.Property;
import com.rentpeeasy.backend.entity.User;
import com.rentpeeasy.backend.exception.ResourceNotFoundException;
import com.rentpeeasy.backend.repository.PropertyRepository;
import com.rentpeeasy.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyService {
    
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    
    public List<PropertyDto> getAllProperties() {
        return propertyRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    public PropertyDto getPropertyById(UUID id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
        return mapToDto(property);
    }
    
    public List<PropertyDto> getFeaturedProperties() {
        return propertyRepository.findByIsFeaturedTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    public List<PropertyDto> searchProperties(
            String city,
            String type,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {
        Property.PropertyType propertyType = null;
        if (type != null && !type.isEmpty()) {
            try {
                propertyType = Property.PropertyType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Invalid type, ignore
            }
        }
        
        return propertyRepository.searchProperties(city, propertyType, minPrice, maxPrice)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public PropertyDto createProperty(PropertyRequest request, String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Property property = Property.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .type(request.getType())
                .city(request.getCity())
                .locality(request.getLocality())
                .price(request.getPrice())
                .priceUnit(request.getPriceUnit() != null ? request.getPriceUnit() : "month")
                .beds(request.getBeds())
                .baths(request.getBaths())
                .squareFeet(request.getSquareFeet())
                .isFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false)
                .isVerified(request.getIsVerified() != null ? request.getIsVerified() : false)
                .images(request.getImages())
                .amenities(request.getAmenities())
                .owner(owner)
                .contactNumber(request.getContactNumber())
                .status(request.getStatus() != null ? request.getStatus() : "AVAILABLE")
                .build();
        
        property = propertyRepository.save(property);
        return mapToDto(property);
    }
    
    @Transactional
    public PropertyDto updateProperty(UUID id, PropertyRequest request, String username) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
        
        // Check if user is the owner
        if (!property.getOwner().getUsername().equals(username)) {
            throw new AccessDeniedException("You don't have permission to update this property");
        }
        
        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setType(request.getType());
        property.setCity(request.getCity());
        property.setLocality(request.getLocality());
        property.setPrice(request.getPrice());
        property.setPriceUnit(request.getPriceUnit());
        property.setBeds(request.getBeds());
        property.setBaths(request.getBaths());
        property.setSquareFeet(request.getSquareFeet());
        property.setImages(request.getImages());
        property.setAmenities(request.getAmenities());
        property.setContactNumber(request.getContactNumber());
        property.setStatus(request.getStatus());
        
        property = propertyRepository.save(property);
        return mapToDto(property);
    }
    
    @Transactional
    public void deleteProperty(UUID id, String username) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
        
        // Check if user is the owner
        if (!property.getOwner().getUsername().equals(username)) {
            throw new AccessDeniedException("You don't have permission to delete this property");
        }
        
        propertyRepository.delete(property);
    }
    
    private PropertyDto mapToDto(Property property) {
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