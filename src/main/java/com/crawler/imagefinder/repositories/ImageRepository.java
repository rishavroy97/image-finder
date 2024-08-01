package com.crawler.imagefinder.repositories;

import com.crawler.imagefinder.models.Image;
import com.crawler.imagefinder.models.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository
        extends JpaRepository<Image, Long> {

    @Modifying
    @Query("DELETE FROM Image i where i.website=?1")
    void deleteByWebsiteId(Website website);
}
