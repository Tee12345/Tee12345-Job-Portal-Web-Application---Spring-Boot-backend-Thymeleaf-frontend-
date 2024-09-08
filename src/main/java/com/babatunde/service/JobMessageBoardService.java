package com.babatunde.service;

import com.babatunde.entity.Company;
import com.babatunde.entity.IProviderJobs;
import com.babatunde.entity.JobMessageBoard;
import com.babatunde.entity.JobProviderJobsDto;
import com.babatunde.entity.Location;
import com.babatunde.repo.JobMessageBoardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class JobMessageBoardService {

    public JobMessageBoardRepo jobMessageBoardRepo;

    @Autowired
    public JobMessageBoardService(JobMessageBoardRepo jobMessageBoardRepo) {
        this.jobMessageBoardRepo = jobMessageBoardRepo;
    }

    public JobMessageBoard addNew(JobMessageBoard jobMessageBoard) {
            return jobMessageBoardRepo.save(jobMessageBoard);
    }
    public List<JobProviderJobsDto> getProviderJobs(int provider) {

        List<IProviderJobs> providerJobsDtos = jobMessageBoardRepo.getProviderJobs(provider);

        List<JobProviderJobsDto> jobProviderJobsDtoList = new ArrayList<>();

        for(IProviderJobs pro : providerJobsDtos) {
                Location loc = new Location(pro.getLocationId(), pro.getCity(), pro.getState(), pro.getCountry());
            Company company = new Company(pro.getCompanyId(), pro.getName(), "");
            jobProviderJobsDtoList.add(new JobProviderJobsDto(pro.getTotalCandidates(), pro.getJob_post_id(),
                    pro.getJob_title(), loc, company));
        }
            return jobProviderJobsDtoList;
    }

    public JobMessageBoard getOne(int id) {

        return jobMessageBoardRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("Job post not available"));
    }

    public List<JobMessageBoard> getAll() {
        return jobMessageBoardRepo.findAll();
    }

    public List<JobMessageBoard> search(String job, String location, List<String> type, List<String> remote, LocalDate searchDate) {

        return Objects.isNull(searchDate) ? jobMessageBoardRepo.searchWithoutDate(job, location, remote, type) :
                jobMessageBoardRepo.search(job, location, remote, type, searchDate);
    }
}
