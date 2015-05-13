package com.aka.bestprice.repository.search;

import com.aka.bestprice.domain.Seller;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Seller entity.
 */
public interface SellerSearchRepository extends ElasticsearchRepository<Seller, Long> {
}
