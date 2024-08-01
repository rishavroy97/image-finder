package com.crawler.imagefinder.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Image")
@Table(name = "image")
@Data
@NoArgsConstructor
public class Image {

    @Id
    @SequenceGenerator(
            name = "image_sequence",
            sequenceName = "image_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "image_sequence"
    )
    @Column(
            name = "image_id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "url",
            nullable = false,
            columnDefinition = "varchar"
    )
    private String url;


    @ManyToOne
    @JoinColumn(
            name = "website_id",
            foreignKey = @ForeignKey(
                    name = "fk_website_details"
            )
    )
    private Website website;

    public Image(String url, Website website) {
        this.url = url;
        this.website = website;
    }
}