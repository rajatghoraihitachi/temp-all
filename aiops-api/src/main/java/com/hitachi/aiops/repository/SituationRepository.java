package com.hitachi.aiops.repository;

import com.hitachi.aiops.domain.Situation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Situation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SituationRepository extends JpaRepository<Situation, Long> {

}
