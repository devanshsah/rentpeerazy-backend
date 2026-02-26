package com.rentpeeasy.backend.repository;

import com.rentpeeasy.backend.entity.Favorite;
import com.rentpeeasy.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
    List<Favorite> findByUserId(UUID userId);
    Optional<Favorite> findByUserIdAndPropertyId(UUID userId, UUID propertyId);
    void deleteByUserIdAndPropertyId(UUID userId, UUID propertyId);
    boolean existsByUserIdAndPropertyId(UUID userId, UUID propertyId);
}