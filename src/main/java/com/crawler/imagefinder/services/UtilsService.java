package com.crawler.imagefinder.services;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class UtilsService {

    private final List<String> imageTypes =
            List.of("jpg", "jpeg", "png", "gif", "ico", "svg");

    @Value("${crawler.max_width}")
    private int maxTreeWidth;

    public Set<String> populateImageURLs(Elements imgElements) {
        Set<String> imageURLs = new HashSet<>();
        for (Element imgElement : imgElements) {
            String imageURL = extractImageURL(imgElement.absUrl("src"));
            if (imageURL == null || imageURL.isEmpty()) continue;
            imageURLs.add(imageURL);
        }
        return imageURLs;
    }

    private String extractImageURL(String src) {
        if (src == null || src.isEmpty()) return null;
        if (src.startsWith("data:image")) return src; // base64 images
        try {
            URL url = new URL(src);
            String decodedURL = URLDecoder.decode(url.toString(), StandardCharsets.UTF_8);

            String extString = decodedURL.substring(decodedURL.lastIndexOf(".") + 1);
            String extension = imageTypes.stream().filter(extString::startsWith).findFirst().orElse(null);
            if (extension == null) {
                return null;
            }
            return decodedURL;
        } catch (MalformedURLException e) {
            System.err.println("Error extracting image URL: " + e.getMessage());
            return null;
        }

    }

    public List<String> populateNewLinks(Elements links, String baseURL, int limit) {

        Set<String> newLinks = new HashSet<>();

        int newLinksCount = 0;
        for (Element link : links) {
            String nextURL = link.absUrl("href");
            if (nextURL.startsWith(baseURL)) {
                newLinks.add(nextURL);
            }
            newLinksCount++;
            if (newLinksCount >= limit) break;
        }

        return new ArrayList<>(newLinks);
    }

    public String getHost(URL currentUrl) {
        String host = currentUrl.getHost();
        String protocol = currentUrl.getProtocol();
        String port = currentUrl.getPort() == -1 ? "" : ":" + currentUrl.getPort();
        return protocol + "://" + host + port;
    }

    public List<String> getTrimmedList(List<String> urlList, Queue<String> urlQ) {
        int keep_count = urlQ.size() >= maxTreeWidth ? 0 : maxTreeWidth - urlQ.size();
        int numToKeep = Math.min(keep_count, urlList.size());
        return new ArrayList<>(urlList.subList(0, numToKeep));
    }

}