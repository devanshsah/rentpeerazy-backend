package com.rentpeeasy.backend.dto;

import com.rentpeeasy.backend.entity.Property;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDto {
    private UUID id;
    private String title;
    private String description;
    private Property.PropertyType type;
    private String city;
    private String locality;
    private BigDecimal price;
    private String priceUnit;
    private Integer beds;
    private Integer baths;
    private Integer squareFeet;
    private Boolean isFeatured;
    private Boolean isVerified;
    private List<String> images;
    private List<String> amenities;
    private UUID ownerId;
    private String ownerName;
    private String contactNumber;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}