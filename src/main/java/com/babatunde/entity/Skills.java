package com.babatunde.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "skills")
@Data
public class Skills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    private String name;
    private String experienceLevel;
    private String yearsOfExperience;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_seeker_profile")
    private CandidateDetails candidateDetails;
}
