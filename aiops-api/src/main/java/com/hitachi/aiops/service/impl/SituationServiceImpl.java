package com.hitachi.aiops.service.impl;

import com.hitachi.aiops.service.SituationService;
import com.hitachi.aiops.domain.Situation;
import com.hitachi.aiops.repository.SituationRepository;
import com.hitachi.aiops.repository.search.SituationSearchRepository;
import com.hitachi.aiops.service.dto.SituationDTO;
import com.hitachi.aiops.service.mapper.SituationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Situation.
 */
@Service
@Transactional
public class SituationServiceImpl implements SituationService {

    private final Logger log = LoggerFactory.getLogger(SituationServiceImpl.class);

    private final SituationRepository situationRepository;

    private final SituationMapper situationMapper;

    private final SituationSearchRepository situationSearchRepository;

    public SituationServiceImpl(SituationRepository situationRepository, SituationMapper situationMapper, SituationSearchRepository situationSearchRepository) {
        this.situationRepository = situationRepository;
        this.situationMapper = situationMapper;
        this.situationSearchRepository = situationSearchRepository;
    }

    /**
     * Save a situation.
     *
     * @param situationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SituationDTO save(SituationDTO situationDTO) {
        log.debug("Request to save Situation : {}", situationDTO);
        Situation situation = situationMapper.toEntity(situationDTO);
        situation = situationRepository.save(situation);
        SituationDTO result = situationMapper.toDto(situation);
        situationSearchRepository.save(situation);
        return result;
    }

    /**
     * Get all the situations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SituationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Situations");
        return situationRepository.findAll(pageable)
            .map(situationMapper::toDto);
    }


    /**
     * Get one situation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SituationDTO> findOne(Long id) {
        log.debug("Request to get Situation : {}", id);
        return situationRepository.findById(id)
            .map(situationMapper::toDto);
    }

    /**
     * Delete the situation by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Situation : {}", id);
        situationRepository.deleteById(id);
        situationSearchRepository.deleteById(id);
    }

    /**
     * Search for the situation corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SituationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Situations for query {}", query);
        return situationSearchRepository.search(queryStringQuery(query), pageable)
            .map(situationMapper::toDto);
    }
}
