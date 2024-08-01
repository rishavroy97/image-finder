package com.crawler.imagefinder.services;

import com.crawler.imagefinder.models.Image;
import com.crawler.imagefinder.models.Website;
import com.crawler.imagefinder.repositories.ImageRepository;
import crawlercommons.robots.BaseRobotRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {

    private final RobotsService robots;
    private final CrawlerService webCrawler;
    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(
            RobotsService robots,
            CrawlerService webCrawler,
            ImageRepository imageRepository
    ) {
        this.robots = robots;
        this.webCrawler = webCrawler;
        this.imageRepository = imageRepository;
    }

    public List<Image> downloadImages(Website website) {

        String url = website.getUrl();
        int maxDepth = website.getLevels() > 0 ? website.getLevels() : 3;
        BaseRobotRules rules = robots.getRules(url);
        List<String> imageURLs = webCrawler.crawlBFS(url, rules, maxDepth);

        List<Image> images = imageURLs
                .stream()
                .map(imgUrl -> new Image(imgUrl, website))
                .toList();

        return imageRepository.saveAll(images);
    }

    public void removeImages(Website website) {
        imageRepository.deleteByWebsiteId(website);
    }
}
