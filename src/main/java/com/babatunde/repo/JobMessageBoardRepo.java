package com.babatunde.repo;

import com.babatunde.entity.IProviderJobs;
import com.babatunde.entity.JobMessageBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface JobMessageBoardRepo extends JpaRepository<JobMessageBoard, Integer> {


    @Query(value = " SELECT COUNT(s.user_id) as totalCandidates,j.job_post_id,j.job_title," +
            "l.id as locationId,l.city,l.state,l.country," + "c.id as companyId,c.name FROM job_message_board j " +
            " inner join location l " +
            " on j.job_location_id = l.id " +
            " INNER join company c  " +
            " on j.job_company_id = c.id " +
            " left join candidate_apply s " +
            " on s.job = j.job_post_id " +
            " where j.posted_by_id = :recruiter " +
            " GROUP By j.job_post_id" ,nativeQuery = true)

    List<IProviderJobs> getProviderJobs(@Param("recruiter") int recruiter);


    @Query(value = "SELECT * FROM job_message_board j INNER JOIN location l on j.job_location_id=l.id  WHERE j" +
            ".job_title LIKE %:job%"
            + " AND (l.city LIKE %:location%"
            + " OR l.country LIKE %:location%"
            + " OR l.state LIKE %:location%) " +
            " AND (j.job_type IN(:type)) " +
            " AND (j.remote IN(:remote)) ", nativeQuery = true)
    List<JobMessageBoard> searchWithoutDate(@Param("job") String job,
                                            @Param("location") String location,
                                            @Param("remote") List<String> remote,
                                            @Param("type") List<String> type);

    @Query(value = "SELECT * FROM job_message_board j INNER JOIN location l on j.job_location_id=l.id  WHERE j" +
            ".job_title LIKE %:job%"
            + " AND (l.city LIKE %:location%"
            + " OR l.country LIKE %:location%"
            + " OR l.state LIKE %:location%) " +
            " AND (j.job_type IN(:type)) " +
            " AND (j.remote IN(:remote)) " +
            " AND (posted_date >= :date)", nativeQuery = true)
    List<JobMessageBoard> search(@Param("job") String job,
                                 @Param("location") String location,
                                 @Param("remote") List<String> remote,
                                 @Param("type") List<String> type,
                                 @Param("date") LocalDate searchDate);

}
