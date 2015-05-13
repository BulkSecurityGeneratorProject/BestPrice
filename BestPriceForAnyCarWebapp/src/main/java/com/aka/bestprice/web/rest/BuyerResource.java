package com.aka.bestprice.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aka.bestprice.domain.Buyer;
import com.aka.bestprice.repository.BuyerRepository;
import com.aka.bestprice.repository.search.BuyerSearchRepository;
import com.aka.bestprice.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Buyer.
 */
@RestController
@RequestMapping("/api")
public class BuyerResource {

    private final Logger log = LoggerFactory.getLogger(BuyerResource.class);

    @Inject
    private BuyerRepository buyerRepository;

    @Inject
    private BuyerSearchRepository buyerSearchRepository;

    /**
     * POST  /buyers -> Create a new buyer.
     */
    @RequestMapping(value = "/buyers",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Buyer buyer) throws URISyntaxException {
        log.debug("REST request to save Buyer : {}", buyer);
        if (buyer.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new buyer cannot already have an ID").build();
        }
        buyerRepository.save(buyer);
        buyerSearchRepository.save(buyer);
        return ResponseEntity.created(new URI("/api/buyers/" + buyer.getId())).build();
    }

    /**
     * PUT  /buyers -> Updates an existing buyer.
     */
    @RequestMapping(value = "/buyers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Buyer buyer) throws URISyntaxException {
        log.debug("REST request to update Buyer : {}", buyer);
        if (buyer.getId() == null) {
            return create(buyer);
        }
        buyerRepository.save(buyer);
        buyerSearchRepository.save(buyer);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /buyers -> get all the buyers.
     */
    @RequestMapping(value = "/buyers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Buyer>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Buyer> page = buyerRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/buyers", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /buyers/:id -> get the "id" buyer.
     */
    @RequestMapping(value = "/buyers/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Buyer> get(@PathVariable Long id) {
        log.debug("REST request to get Buyer : {}", id);
        return Optional.ofNullable(buyerRepository.findOne(id))
            .map(buyer -> new ResponseEntity<>(
                buyer,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /buyers/:id -> delete the "id" buyer.
     */
    @RequestMapping(value = "/buyers/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Buyer : {}", id);
        buyerRepository.delete(id);
        buyerSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/buyers/:query -> search for the buyer corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/buyers/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Buyer> search(@PathVariable String query) {
        return StreamSupport
            .stream(buyerSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
