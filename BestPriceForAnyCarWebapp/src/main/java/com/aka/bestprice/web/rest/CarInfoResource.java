package com.aka.bestprice.web.rest;

import com.aka.bestprice.domain.Car;
import com.aka.bestprice.repository.CarRepository;
import com.aka.bestprice.repository.search.CarSearchRepository;
import com.aka.bestprice.service.CarInfoService;
import com.aka.bestprice.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
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

import static org.elasticsearch.index.query.QueryBuilders.queryString;

/**
 * REST controller for managing Car.
 */
@RestController
@RequestMapping("/api")
public class CarInfoResource {

    private final Logger log = LoggerFactory.getLogger(CarInfoResource.class);

    @Inject
    private CarInfoService carInfoService;

    /**
     * GET  /carinfo/:vrn -> get the "vrn" car.
     */
    @RequestMapping(value = "/carinfo/{vrn}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Car> get(@PathVariable String vrn) {
        log.debug("REST request to get Car : {}", vrn);
        return Optional.ofNullable(carInfoService.lookup(vrn))
            .map(car -> new ResponseEntity<>(
                car,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
