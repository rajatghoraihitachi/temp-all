package com.hitachi.aiops.repository.search;

import com.hitachi.aiops.domain.Situation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Situation entity.
 */
public interface SituationSearchRepository extends ElasticsearchRepository<Situation, Long> {
}
