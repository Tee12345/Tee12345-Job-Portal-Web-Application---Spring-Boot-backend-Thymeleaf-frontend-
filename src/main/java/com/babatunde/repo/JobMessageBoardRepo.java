package com.babatunde.repo;

import com.babatunde.entity.IProviderJobs;
import com.babatunde.entity.JobMessageBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobMessageBoardRepo extends JpaRepository<JobMessageBoard, Integer> {
    @Query(value = " SELECT COUNT(s.user_id) as totalCandidates,j.job_post_id,j.job_title,l.id as locationId,l.city,l.state,l.country,c.id as companyId,c.name FROM job_message_board j " +
            " inner join location l " +
            " on j.job_location_id = l.id " +
            " INNER join company c  " +
            " on j.job_company_id = c.id " +
            " left join candidate_apply s " +
            " on s.job = j.job_post_id " +
            " where j.posted_by_id = :recruiter " +
            " GROUP By j.job_post_id" ,nativeQuery = true)

    List<IProviderJobs> getProviderJobs(@Param("recruiter") int recruiter);

}
