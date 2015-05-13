package com.aka.bestprice.web.rest;

import com.aka.bestprice.Application;
import com.aka.bestprice.domain.Seller;
import com.aka.bestprice.repository.SellerRepository;
import com.aka.bestprice.repository.search.SellerSearchRepository;

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
 * Test class for the SellerResource REST controller.
 *
 * @see SellerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SellerResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_CITY = "SAMPLE_TEXT";
    private static final String UPDATED_CITY = "UPDATED_TEXT";
    private static final String DEFAULT_POSTCODE = "SAMPLE_TEXT";
    private static final String UPDATED_POSTCODE = "UPDATED_TEXT";

    @Inject
    private SellerRepository sellerRepository;

    @Inject
    private SellerSearchRepository sellerSearchRepository;

    private MockMvc restSellerMockMvc;

    private Seller seller;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SellerResource sellerResource = new SellerResource();
        ReflectionTestUtils.setField(sellerResource, "sellerRepository", sellerRepository);
        ReflectionTestUtils.setField(sellerResource, "sellerSearchRepository", sellerSearchRepository);
        this.restSellerMockMvc = MockMvcBuilders.standaloneSetup(sellerResource).build();
    }

    @Before
    public void initTest() {
        seller = new Seller();
        seller.setName(DEFAULT_NAME);
        seller.setCity(DEFAULT_CITY);
        seller.setPostcode(DEFAULT_POSTCODE);
    }

    @Test
    @Transactional
    public void createSeller() throws Exception {
        int databaseSizeBeforeCreate = sellerRepository.findAll().size();

        // Create the Seller
        restSellerMockMvc.perform(post("/api/sellers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(seller)))
                .andExpect(status().isCreated());

        // Validate the Seller in the database
        List<Seller> sellers = sellerRepository.findAll();
        assertThat(sellers).hasSize(databaseSizeBeforeCreate + 1);
        Seller testSeller = sellers.get(sellers.size() - 1);
        assertThat(testSeller.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSeller.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testSeller.getPostcode()).isEqualTo(DEFAULT_POSTCODE);
    }

    @Test
    @Transactional
    public void getAllSellers() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellers
        restSellerMockMvc.perform(get("/api/sellers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(seller.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].postcode").value(hasItem(DEFAULT_POSTCODE.toString())));
    }

    @Test
    @Transactional
    public void getSeller() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get the seller
        restSellerMockMvc.perform(get("/api/sellers/{id}", seller.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(seller.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.postcode").value(DEFAULT_POSTCODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSeller() throws Exception {
        // Get the seller
        restSellerMockMvc.perform(get("/api/sellers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSeller() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

		int databaseSizeBeforeUpdate = sellerRepository.findAll().size();

        // Update the seller
        seller.setName(UPDATED_NAME);
        seller.setCity(UPDATED_CITY);
        seller.setPostcode(UPDATED_POSTCODE);
        restSellerMockMvc.perform(put("/api/sellers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(seller)))
                .andExpect(status().isOk());

        // Validate the Seller in the database
        List<Seller> sellers = sellerRepository.findAll();
        assertThat(sellers).hasSize(databaseSizeBeforeUpdate);
        Seller testSeller = sellers.get(sellers.size() - 1);
        assertThat(testSeller.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSeller.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testSeller.getPostcode()).isEqualTo(UPDATED_POSTCODE);
    }

    @Test
    @Transactional
    public void deleteSeller() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

		int databaseSizeBeforeDelete = sellerRepository.findAll().size();

        // Get the seller
        restSellerMockMvc.perform(delete("/api/sellers/{id}", seller.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Seller> sellers = sellerRepository.findAll();
        assertThat(sellers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
