package com.hitachi.aiops.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of SituationSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class SituationSearchRepositoryMockConfiguration {

    @MockBean
    private SituationSearchRepository mockSituationSearchRepository;

}
