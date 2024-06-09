package com.fabhotel.Eras.repository;

import java.util.List;

import com.fabhotel.Eras.entity.EndorsementEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EndorsementRepository extends JpaRepository<EndorsementEntity, Long> {

	List<EndorsementEntity> findByRevieweeId(String userId);
	// Method to count endorsements based on reviewerId, revieweeId, and skillId
    @Query("SELECT COUNT(e) FROM EndorsementEntity e WHERE e.reviewerId = :reviewerId AND e.revieweeId = :revieweeId AND e.skill.id = :skillId")
    int countByReviewerIdAndRevieweeIdAndSkillId(@Param("reviewerId") String reviewerId, 
                                                  @Param("revieweeId") String revieweeId, 
                                                  @Param("skillId") Long skillId);
	

}
