package com.babatunde.entity;

import lombok.Data;

@Data
public class JobProviderJobsDto {

    private Long totalCandidates;
    private Integer jobPostId;
    private String jobTitle;
    private Location jobLocationId;
    private Company jobCompanyId;

    public JobProviderJobsDto() {}

    public JobProviderJobsDto(Long totalCandidates, Integer jobPostId, String jobTitle, Location jobLocationId, Company jobCompanyId) {
        this.totalCandidates = totalCandidates;
        this.jobPostId = jobPostId;
        this.jobTitle = jobTitle;
        this.jobLocationId = jobLocationId;
        this.jobCompanyId = jobCompanyId;
    }
}
