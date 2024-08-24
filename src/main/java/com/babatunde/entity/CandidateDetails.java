package com.babatunde.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "candidate_details")
@Data
public class CandidateDetails {

    @Id
    private int userAccountId;
    public CandidateDetails() {}

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private Users userId;

    private String firstName;
    private String lastName;
    private String city;
    private String state;
    private String country;
    private String workAuthorization;
    private String employmentType;
    private String resume;
    @Column(nullable = true, length = 64)
    private String profilePhoto;

    @OneToMany(targetEntity = Experience.class, cascade = CascadeType.ALL, mappedBy = "candidateDetails")
    private List<Experience> experiences;

    public CandidateDetails(Users users) {
        this.userId = users;
    }


}
