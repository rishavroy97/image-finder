package com.crawler.imagefinder.services;

import com.crawler.imagefinder.exceptions.ClientSideException;
import crawlercommons.robots.BaseRobotRules;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class CrawlerService {

    private final UtilsService utils;
    private final ExecutorService executorService;

    @Value("${crawler.user_agent}")
    private String userAgent;

    @Value("${crawler.num_links_per_url}")
    private Integer numLinksPerURL;

    @Autowired
    public CrawlerService(
            UtilsService utils,
            @Value("${crawler.n_threads}") final int nThreads
    ) {
        this.utils = utils;
        this.executorService = Executors.newScheduledThreadPool(nThreads);
    }

    /**
     * Multithreaded operation to fetch images from all the given URLs
     *
     * @param webURLs - List of URLs
     */
    private void fetchImages(
            List<String> webURLs,
            Long delay,
            String baseURL,
            Queue<String> queue,
            Set<String> imageURLs
    ) {
        List<CompletableFuture<Set<String>>> futures = webURLs.stream()
                .map(url -> CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        System.err.println(e.getMessage());
                        return new HashSet<String>();
                    }
                    return crawlURL(url, baseURL, queue);
                }, executorService))
                .toList();

        CompletableFuture<Void> allFutures = CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]));

        allFutures.thenRun(() -> {
            Set<String> result = futures.stream()
                    .map(CompletableFuture::join)
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());

            System.out.println("New images found: " + result.size());
            imageURLs.addAll(result);
        });
        allFutures.join();
    }

    /**
     * Perform a Breadth-First Search over the website, starting at baseURL
     *
     * @param baseURL  - starting URL
     * @param maxDepth - depth of the BFS tree
     * @return - list of image urls from the website
     */
    public List<String> crawlBFS(String baseURL, BaseRobotRules rules, int maxDepth) {

        long startTime = System.currentTimeMillis();

        Set<String> imageURLs = new ConcurrentSkipListSet<>();
        Queue<String> queue = new ConcurrentLinkedQueue<>();
        Set<String> visitedURLs = new ConcurrentSkipListSet<>();

        queue.offer(baseURL);
        int currentDepth = 0;

        while (!queue.isEmpty() && currentDepth < maxDepth) {
            List<String> crawllableURLs = queue
                    .stream()
                    .filter(rules::isAllowed)
                    .filter(url -> !visitedURLs.contains(url))
                    .collect(Collectors.toList());
            queue.clear();

            fetchImages(crawllableURLs, rules.getCrawlDelay(), baseURL, queue, imageURLs);
//            fetchImages2(crawllableURLs, rules.getCrawlDelay(), baseURL, queue, imageURLs);
            currentDepth++;
            System.out.println("Crawling level " + currentDepth + " of " + maxDepth);
            System.out.println("New urls found: " + queue.size());
            visitedURLs.addAll(crawllableURLs);
        }
        System.out.println("Crawling finished");
        List<String> images = new ArrayList<>(imageURLs);

        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");

        return images;
    }

    /**
     * Task Method - Crawls a single URL and returns the images
     *
     * @param url     - webpage URL to be scraped
     * @param baseURL - baseURL of the webpage
     * @param queue   - Queue of Pending URLs
     * @return - Set of Image URLs
     */
    private Set<String> crawlURL(String url, String baseURL, Queue<String> queue) {
        try {
            Document document = Jsoup.connect(url).userAgent(userAgent).get();

            Elements imgElements = document.select("img");
            Set<String> imageURLs = utils.populateImageURLs(imgElements);

            Elements links = document.select("a[href]");
            List<String> newLinks = utils.populateNewLinks(links, baseURL, numLinksPerURL);

            List<String> trimmedList = utils.getTrimmedList(newLinks, queue);
            queue.addAll(trimmedList);

            return imageURLs;
        } catch (IOException e) {
            System.out.println("Error connecting to url: " + url);
            System.err.println(e);
            return Set.of();
        }
    }
}
