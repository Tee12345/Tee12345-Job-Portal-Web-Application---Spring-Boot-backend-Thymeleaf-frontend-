package com.babatunde.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "experience")
@Data
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    private String name;
    private String experienceLevel;
    private String yearsOfExperience;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "candidate_details")
    private CandidateDetails candidateDetails;
}
