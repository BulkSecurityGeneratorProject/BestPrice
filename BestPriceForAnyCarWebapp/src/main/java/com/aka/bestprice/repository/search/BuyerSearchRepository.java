package com.aka.bestprice.repository.search;

import com.aka.bestprice.domain.Buyer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Buyer entity.
 */
public interface BuyerSearchRepository extends ElasticsearchRepository<Buyer, Long> {
}
