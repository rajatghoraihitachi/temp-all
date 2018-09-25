package com.hitachi.aiops.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hitachi.aiops.service.SituationService;
import com.hitachi.aiops.web.rest.errors.BadRequestAlertException;
import com.hitachi.aiops.web.rest.util.HeaderUtil;
import com.hitachi.aiops.web.rest.util.PaginationUtil;
import com.hitachi.aiops.service.dto.SituationDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Situation.
 */
@RestController
@RequestMapping("/api")
public class SituationResource {

    private final Logger log = LoggerFactory.getLogger(SituationResource.class);

    private static final String ENTITY_NAME = "aiopsapiSituation";

    private final SituationService situationService;

    public SituationResource(SituationService situationService) {
        this.situationService = situationService;
    }

    /**
     * POST  /situations : Create a new situation.
     *
     * @param situationDTO the situationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new situationDTO, or with status 400 (Bad Request) if the situation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/situations")
    @Timed
    public ResponseEntity<SituationDTO> createSituation(@RequestBody SituationDTO situationDTO) throws URISyntaxException {
        log.debug("REST request to save Situation : {}", situationDTO);
        if (situationDTO.getId() != null) {
            throw new BadRequestAlertException("A new situation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SituationDTO result = situationService.save(situationDTO);
        return ResponseEntity.created(new URI("/api/situations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /situations : Updates an existing situation.
     *
     * @param situationDTO the situationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated situationDTO,
     * or with status 400 (Bad Request) if the situationDTO is not valid,
     * or with status 500 (Internal Server Error) if the situationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/situations")
    @Timed
    public ResponseEntity<SituationDTO> updateSituation(@RequestBody SituationDTO situationDTO) throws URISyntaxException {
        log.debug("REST request to update Situation : {}", situationDTO);
        if (situationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SituationDTO result = situationService.save(situationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, situationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /situations : get all the situations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of situations in body
     */
    @GetMapping("/situations")
    @Timed
    public ResponseEntity<List<SituationDTO>> getAllSituations(Pageable pageable) {
        log.debug("REST request to get a page of Situations");
        Page<SituationDTO> page = situationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/situations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /situations/:id : get the "id" situation.
     *
     * @param id the id of the situationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the situationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/situations/{id}")
    @Timed
    public ResponseEntity<SituationDTO> getSituation(@PathVariable Long id) {
        log.debug("REST request to get Situation : {}", id);
        Optional<SituationDTO> situationDTO = situationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(situationDTO);
    }

    /**
     * DELETE  /situations/:id : delete the "id" situation.
     *
     * @param id the id of the situationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/situations/{id}")
    @Timed
    public ResponseEntity<Void> deleteSituation(@PathVariable Long id) {
        log.debug("REST request to delete Situation : {}", id);
        situationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/situations?query=:query : search for the situation corresponding
     * to the query.
     *
     * @param query the query of the situation search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/situations")
    @Timed
    public ResponseEntity<List<SituationDTO>> searchSituations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Situations for query {}", query);
        Page<SituationDTO> page = situationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/situations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
