package com.medcompare.backend.entity;
import java.time.LocalDateTime;


import jakarta.persistence.*;

@Entity
@Table(name = "medicine")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    private Double onemgPrice;
    private String onemgUrl;
    private Double apolloPrice;
    private String apolloUrl;

    private Double pharmeasyPrice;
    private String pharmeasyUrl;
    @Column
private LocalDateTime priceUpdatedAt;

// Add getter and setter:
public LocalDateTime getPriceUpdatedAt() { return priceUpdatedAt; }
public void setPriceUpdatedAt(LocalDateTime priceUpdatedAt) { this.priceUpdatedAt = priceUpdatedAt; }
   
@Column(name = "description")
    private String desc;
    @Column(nullable = false)
    private String name;
    private String imageUrl;

    public Double getOnemgPrice() {
        return onemgPrice;
    }

    public void setOnemgPrice(Double onemgPrice) {
        this.onemgPrice = onemgPrice;
    }

    public String getOnemgUrl() {
        return onemgUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setOnemgUrl(String onemgUrl) {
        this.onemgUrl = onemgUrl;
    }

    public Double getApolloPrice() {
        return apolloPrice;
    }

    public void setApolloPrice(Double apolloPrice) {
        this.apolloPrice = apolloPrice;
    }

    public String getApolloUrl() {
        return apolloUrl;
    }

    public void setApolloUrl(String apolloUrl) {
        this.apolloUrl = apolloUrl;
    }

    public Double getPharmeasyPrice() {
        return pharmeasyPrice;
    }

    public void setPharmeasyPrice(Double pharmeasyPrice) {
        this.pharmeasyPrice = pharmeasyPrice;
    }

    public String getPharmeasyUrl() {
        return pharmeasyUrl;
    }

    public void setPharmeasyUrl(String pharmeasyUrl) {
        this.pharmeasyUrl = pharmeasyUrl;
    }

    // Constructors
    public Medicine() {
    }

    public Medicine(String name ) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
