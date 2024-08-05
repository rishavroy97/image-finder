package com.crawler.imagefinder.controllers;

import com.crawler.imagefinder.models.Website;
import com.crawler.imagefinder.services.WebsiteService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;

import static org.mockito.Mockito.doReturn;

@GraphQlTest(WebsiteController.class)
public class WebsiteControllerTests {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    WebsiteService websiteServiceMock;

    static Website website = new Website();


    @BeforeAll
    static void setUp() {
        website.setName("Wikipedia");
        website.setUrl("https://www.wikipedia.org/");
    }


    @Test
    void shouldGetWikipedia() {
        List<Website> websites = List.of(website);
        doReturn(websites).when(websiteServiceMock).getAllWebsites();
        graphQlTester
                .documentName("websiteDetails")
                .execute()
                .path("websites")
                .matchesJson("""
                            [{
                                "name": "Wikipedia",
                                "url": "https://www.wikipedia.org/"
                            }]
                        """);
    }
}
