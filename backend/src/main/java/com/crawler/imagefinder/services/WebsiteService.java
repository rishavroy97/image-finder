package com.crawler.imagefinder.services;

import com.crawler.imagefinder.models.Image;
import com.crawler.imagefinder.models.Website;
import com.crawler.imagefinder.repositories.WebsiteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WebsiteService {

    private final WebsiteRepository websiteRepository;
    private final ImageService imageService;

    @Autowired
    public WebsiteService(
            WebsiteRepository websiteRepository,
            ImageService imageService
    ) {
        this.websiteRepository = websiteRepository;
        this.imageService = imageService;
    }

    public Website getWebsiteById(UUID id) {
        Optional<Website> website = websiteRepository.findById(id);

        if (website.isEmpty()) {
            throw new IllegalStateException("Website does not exist");
        }

        return website.get();
    }

    public List<Website> getAllWebsites() {
        return websiteRepository.findAll();
    }

    @Transactional
    public Website upsertImages(Website website) {
        String url = website.getUrl();
        int levels = website.getLevels();

        Optional<Website> existingWebsite = websiteRepository.findByURL(url);

        if (existingWebsite.isPresent() && existingWebsite.get().getLevels() == levels) {
            return existingWebsite.get();
        }

        Website updatedWebsite = saveNewWebsite(website, existingWebsite);
        return updateImages(updatedWebsite);
    }

    private Website saveNewWebsite(Website website, Optional<Website> existingWebsite) {
        if(existingWebsite.isPresent()) {
            existingWebsite.get().setName(website.getName());
            existingWebsite.get().setUrl(website.getUrl());
            existingWebsite.get().setLevels(website.getLevels());
        }
        return websiteRepository.save(website);
    }

    private Website updateImages(Website website) {
        imageService.removeImages(website);
        List<Image> images = imageService.downloadImages(website);
        website.setImages(images);

        return websiteRepository.save(website);
    }
}
