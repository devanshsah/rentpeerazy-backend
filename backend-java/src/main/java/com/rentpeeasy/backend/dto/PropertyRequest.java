package com.rentpeeasy.backend.dto;

import com.rentpeeasy.backend.entity.Property;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyRequest {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    @NotNull(message = "Property type is required")
    private Property.PropertyType type;
    
    @NotBlank(message = "City is required")
    private String city;
    
    @NotBlank(message = "Locality is required")
    private String locality;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    private String priceUnit;
    private Integer beds;
    private Integer baths;
    private Integer squareFeet;
    private Boolean isFeatured;
    private Boolean isVerified;
    private List<String> images;
    private List<String> amenities;
    private String contactNumber;
    private String status;
}