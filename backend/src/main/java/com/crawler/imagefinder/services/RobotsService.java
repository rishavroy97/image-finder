package com.crawler.imagefinder.services;

import com.crawler.imagefinder.exceptions.ClientSideException;
import com.crawler.imagefinder.exceptions.ServerSideException;
import crawlercommons.robots.BaseRobotRules;
import crawlercommons.robots.SimpleRobotRules;
import crawlercommons.robots.SimpleRobotRulesParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class RobotsService {
    private final Map<String, BaseRobotRules> robotsTxtRules;
    private final UtilsService utils;

    @Value("${crawler.user_agent}")
    private String userAgent;

    @Autowired
    public RobotsService(UtilsService utils) {
        this.robotsTxtRules = new HashMap<>();
        this.utils = utils;
    }


    public BaseRobotRules getRules(String url) {
        URL currentUrl;
        try {
            currentUrl = new URL(url);
        } catch (MalformedURLException e) {
            throw new ClientSideException("Malformed URL: " + url, e);
        }
        String hostId = utils.getHost(currentUrl);
        BaseRobotRules rules = getRobotRules(hostId);
        if (!rules.isAllowed(hostId)) {
            throw new ClientSideException("Forbidden URL (Illegal to crawl): " + url);
        }
        return rules;
    }

    private BaseRobotRules getRobotRules(String hostId) {
        BaseRobotRules rules = robotsTxtRules.get(hostId);
        if (rules != null) return rules;

        String robotsURL = hostId + "/robots.txt";
        HttpRequest robotsRequest;
        HttpResponse<String> response;

        try {
            robotsRequest = HttpRequest.newBuilder()
                    .uri(new URI(robotsURL))
                    .GET()
                    .build();
        } catch (URISyntaxException | IllegalArgumentException e) {
            throw new ClientSideException("Malformed URL: " + robotsURL, e);
        }

        try {
            response = HttpClient.newBuilder()
                    .build()
                    .send(robotsRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new ClientSideException("Invalid URL: " + robotsURL, e);
        } catch (InterruptedException e) {
            throw new ServerSideException();
        }

        if (response.statusCode() != 200) {
            rules = new SimpleRobotRules(SimpleRobotRules.RobotRulesMode.ALLOW_ALL);
            rules.setCrawlDelay(2000L);
            robotsTxtRules.put(hostId, rules);
            return rules;
        }

        SimpleRobotRulesParser robotParser = new SimpleRobotRulesParser();
        rules = robotParser.parseContent(hostId, response.body().getBytes(StandardCharsets.UTF_8),
                "text/plain", Collections.singletonList(userAgent));
        if (rules.getCrawlDelay() < 0) {
            rules.setCrawlDelay(2000L);
        }
        robotsTxtRules.put(hostId, rules);
        System.out.println("Robots rules parsed: " + rules);
        return rules;
    }
}
