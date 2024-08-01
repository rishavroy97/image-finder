package com.crawler.imagefinder.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity(name = "Website")
@Table(name = "website")
@NoArgsConstructor
public class Website {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "website_id",
            updatable = false
    )
    private UUID id;

    @Column(
            name = "website_url",
            nullable = false
    )
    private String url;

    @Column(name = "website_name")
    private String name;

    @Column(
            name = "level",
            columnDefinition = "Integer",
            nullable = false
    )
    private Integer levels;

    @OneToMany
    @JoinColumn(
            name = "website_id"
    )
    private List<Image> images;

    public Website(String url, String name, Integer levels) {
        this.url = url;
        this.name = name;
        this.levels = levels;
    }
}
