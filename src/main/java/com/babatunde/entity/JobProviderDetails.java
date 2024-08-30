package com.babatunde.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "job_provider_details")
@Data
public class JobProviderDetails {

    @Id
    private int userAccountId;

    public JobProviderDetails() {}

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private Users  userId;

    private String firstName;
    private String lastName;
    private String city;
    private String state;
    private String country;
    private String company;
    @Column(nullable = true, length = 64)
    private String profilePhoto;

    @Transient
    public String getPhotosImagePath() {
        if(profilePhoto == null) return null;
        return "/photos/provider/" + userAccountId + "/" + profilePhoto;
    }

    public JobProviderDetails(Users users) {
        this.userId = users;
    }
}
