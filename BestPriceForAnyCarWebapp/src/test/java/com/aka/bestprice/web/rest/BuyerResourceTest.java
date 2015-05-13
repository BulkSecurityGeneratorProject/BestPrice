package com.aka.bestprice.web.rest;

import com.aka.bestprice.Application;
import com.aka.bestprice.domain.Buyer;
import com.aka.bestprice.repository.BuyerRepository;
import com.aka.bestprice.repository.search.BuyerSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BuyerResource REST controller.
 *
 * @see BuyerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BuyerResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private BuyerRepository buyerRepository;

    @Inject
    private BuyerSearchRepository buyerSearchRepository;

    private MockMvc restBuyerMockMvc;

    private Buyer buyer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BuyerResource buyerResource = new BuyerResource();
        ReflectionTestUtils.setField(buyerResource, "buyerRepository", buyerRepository);
        ReflectionTestUtils.setField(buyerResource, "buyerSearchRepository", buyerSearchRepository);
        this.restBuyerMockMvc = MockMvcBuilders.standaloneSetup(buyerResource).build();
    }

    @Before
    public void initTest() {
        buyer = new Buyer();
        buyer.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createBuyer() throws Exception {
        int databaseSizeBeforeCreate = buyerRepository.findAll().size();

        // Create the Buyer
        restBuyerMockMvc.perform(post("/api/buyers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(buyer)))
                .andExpect(status().isCreated());

        // Validate the Buyer in the database
        List<Buyer> buyers = buyerRepository.findAll();
        assertThat(buyers).hasSize(databaseSizeBeforeCreate + 1);
        Buyer testBuyer = buyers.get(buyers.size() - 1);
        assertThat(testBuyer.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllBuyers() throws Exception {
        // Initialize the database
        buyerRepository.saveAndFlush(buyer);

        // Get all the buyers
        restBuyerMockMvc.perform(get("/api/buyers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(buyer.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getBuyer() throws Exception {
        // Initialize the database
        buyerRepository.saveAndFlush(buyer);

        // Get the buyer
        restBuyerMockMvc.perform(get("/api/buyers/{id}", buyer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(buyer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBuyer() throws Exception {
        // Get the buyer
        restBuyerMockMvc.perform(get("/api/buyers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBuyer() throws Exception {
        // Initialize the database
        buyerRepository.saveAndFlush(buyer);

		int databaseSizeBeforeUpdate = buyerRepository.findAll().size();

        // Update the buyer
        buyer.setName(UPDATED_NAME);
        restBuyerMockMvc.perform(put("/api/buyers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(buyer)))
                .andExpect(status().isOk());

        // Validate the Buyer in the database
        List<Buyer> buyers = buyerRepository.findAll();
        assertThat(buyers).hasSize(databaseSizeBeforeUpdate);
        Buyer testBuyer = buyers.get(buyers.size() - 1);
        assertThat(testBuyer.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteBuyer() throws Exception {
        // Initialize the database
        buyerRepository.saveAndFlush(buyer);

		int databaseSizeBeforeDelete = buyerRepository.findAll().size();

        // Get the buyer
        restBuyerMockMvc.perform(delete("/api/buyers/{id}", buyer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Buyer> buyers = buyerRepository.findAll();
        assertThat(buyers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
