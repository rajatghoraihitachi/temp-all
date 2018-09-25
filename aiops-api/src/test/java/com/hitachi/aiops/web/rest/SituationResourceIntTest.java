package com.hitachi.aiops.web.rest;

import com.hitachi.aiops.AiopsapiApp;

import com.hitachi.aiops.domain.Situation;
import com.hitachi.aiops.repository.SituationRepository;
import com.hitachi.aiops.repository.search.SituationSearchRepository;
import com.hitachi.aiops.service.SituationService;
import com.hitachi.aiops.service.dto.SituationDTO;
import com.hitachi.aiops.service.mapper.SituationMapper;
import com.hitachi.aiops.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.hitachi.aiops.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SituationResource REST controller.
 *
 * @see SituationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AiopsapiApp.class)
public class SituationResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private SituationRepository situationRepository;

    @Autowired
    private SituationMapper situationMapper;
    
    @Autowired
    private SituationService situationService;

    /**
     * This repository is mocked in the com.hitachi.aiops.repository.search test package.
     *
     * @see com.hitachi.aiops.repository.search.SituationSearchRepositoryMockConfiguration
     */
    @Autowired
    private SituationSearchRepository mockSituationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSituationMockMvc;

    private Situation situation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SituationResource situationResource = new SituationResource(situationService);
        this.restSituationMockMvc = MockMvcBuilders.standaloneSetup(situationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Situation createEntity(EntityManager em) {
        Situation situation = new Situation()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE);
        return situation;
    }

    @Before
    public void initTest() {
        situation = createEntity(em);
    }

    @Test
    @Transactional
    public void createSituation() throws Exception {
        int databaseSizeBeforeCreate = situationRepository.findAll().size();

        // Create the Situation
        SituationDTO situationDTO = situationMapper.toDto(situation);
        restSituationMockMvc.perform(post("/api/situations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(situationDTO)))
            .andExpect(status().isCreated());

        // Validate the Situation in the database
        List<Situation> situationList = situationRepository.findAll();
        assertThat(situationList).hasSize(databaseSizeBeforeCreate + 1);
        Situation testSituation = situationList.get(situationList.size() - 1);
        assertThat(testSituation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSituation.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the Situation in Elasticsearch
        verify(mockSituationSearchRepository, times(1)).save(testSituation);
    }

    @Test
    @Transactional
    public void createSituationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = situationRepository.findAll().size();

        // Create the Situation with an existing ID
        situation.setId(1L);
        SituationDTO situationDTO = situationMapper.toDto(situation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSituationMockMvc.perform(post("/api/situations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(situationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Situation in the database
        List<Situation> situationList = situationRepository.findAll();
        assertThat(situationList).hasSize(databaseSizeBeforeCreate);

        // Validate the Situation in Elasticsearch
        verify(mockSituationSearchRepository, times(0)).save(situation);
    }

    @Test
    @Transactional
    public void getAllSituations() throws Exception {
        // Initialize the database
        situationRepository.saveAndFlush(situation);

        // Get all the situationList
        restSituationMockMvc.perform(get("/api/situations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(situation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getSituation() throws Exception {
        // Initialize the database
        situationRepository.saveAndFlush(situation);

        // Get the situation
        restSituationMockMvc.perform(get("/api/situations/{id}", situation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(situation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSituation() throws Exception {
        // Get the situation
        restSituationMockMvc.perform(get("/api/situations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSituation() throws Exception {
        // Initialize the database
        situationRepository.saveAndFlush(situation);

        int databaseSizeBeforeUpdate = situationRepository.findAll().size();

        // Update the situation
        Situation updatedSituation = situationRepository.findById(situation.getId()).get();
        // Disconnect from session so that the updates on updatedSituation are not directly saved in db
        em.detach(updatedSituation);
        updatedSituation
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE);
        SituationDTO situationDTO = situationMapper.toDto(updatedSituation);

        restSituationMockMvc.perform(put("/api/situations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(situationDTO)))
            .andExpect(status().isOk());

        // Validate the Situation in the database
        List<Situation> situationList = situationRepository.findAll();
        assertThat(situationList).hasSize(databaseSizeBeforeUpdate);
        Situation testSituation = situationList.get(situationList.size() - 1);
        assertThat(testSituation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSituation.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the Situation in Elasticsearch
        verify(mockSituationSearchRepository, times(1)).save(testSituation);
    }

    @Test
    @Transactional
    public void updateNonExistingSituation() throws Exception {
        int databaseSizeBeforeUpdate = situationRepository.findAll().size();

        // Create the Situation
        SituationDTO situationDTO = situationMapper.toDto(situation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSituationMockMvc.perform(put("/api/situations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(situationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Situation in the database
        List<Situation> situationList = situationRepository.findAll();
        assertThat(situationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Situation in Elasticsearch
        verify(mockSituationSearchRepository, times(0)).save(situation);
    }

    @Test
    @Transactional
    public void deleteSituation() throws Exception {
        // Initialize the database
        situationRepository.saveAndFlush(situation);

        int databaseSizeBeforeDelete = situationRepository.findAll().size();

        // Get the situation
        restSituationMockMvc.perform(delete("/api/situations/{id}", situation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Situation> situationList = situationRepository.findAll();
        assertThat(situationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Situation in Elasticsearch
        verify(mockSituationSearchRepository, times(1)).deleteById(situation.getId());
    }

    @Test
    @Transactional
    public void searchSituation() throws Exception {
        // Initialize the database
        situationRepository.saveAndFlush(situation);
        when(mockSituationSearchRepository.search(queryStringQuery("id:" + situation.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(situation), PageRequest.of(0, 1), 1));
        // Search the situation
        restSituationMockMvc.perform(get("/api/_search/situations?query=id:" + situation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(situation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Situation.class);
        Situation situation1 = new Situation();
        situation1.setId(1L);
        Situation situation2 = new Situation();
        situation2.setId(situation1.getId());
        assertThat(situation1).isEqualTo(situation2);
        situation2.setId(2L);
        assertThat(situation1).isNotEqualTo(situation2);
        situation1.setId(null);
        assertThat(situation1).isNotEqualTo(situation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SituationDTO.class);
        SituationDTO situationDTO1 = new SituationDTO();
        situationDTO1.setId(1L);
        SituationDTO situationDTO2 = new SituationDTO();
        assertThat(situationDTO1).isNotEqualTo(situationDTO2);
        situationDTO2.setId(situationDTO1.getId());
        assertThat(situationDTO1).isEqualTo(situationDTO2);
        situationDTO2.setId(2L);
        assertThat(situationDTO1).isNotEqualTo(situationDTO2);
        situationDTO1.setId(null);
        assertThat(situationDTO1).isNotEqualTo(situationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(situationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(situationMapper.fromId(null)).isNull();
    }
}
