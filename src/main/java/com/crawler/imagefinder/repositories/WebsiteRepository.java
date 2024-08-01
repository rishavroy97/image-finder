package com.crawler.imagefinder.repositories;

import com.crawler.imagefinder.models.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WebsiteRepository
        extends JpaRepository<Website, UUID> {

    @Query("SELECT w FROM Website w WHERE w.url = ?1")
    Optional<Website> findByURL(String url);
}
