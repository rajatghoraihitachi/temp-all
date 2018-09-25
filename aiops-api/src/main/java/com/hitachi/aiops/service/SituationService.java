package com.hitachi.aiops.service;

import com.hitachi.aiops.service.dto.SituationDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Situation.
 */
public interface SituationService {

    /**
     * Save a situation.
     *
     * @param situationDTO the entity to save
     * @return the persisted entity
     */
    SituationDTO save(SituationDTO situationDTO);

    /**
     * Get all the situations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SituationDTO> findAll(Pageable pageable);


    /**
     * Get the "id" situation.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SituationDTO> findOne(Long id);

    /**
     * Delete the "id" situation.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the situation corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SituationDTO> search(String query, Pageable pageable);
}
