
package com.fabhotel.Eras.repository;

import com.fabhotel.Eras.entity.SkillEntity;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<SkillEntity, Long> {
	
	SkillEntity findByName(String name);
}
