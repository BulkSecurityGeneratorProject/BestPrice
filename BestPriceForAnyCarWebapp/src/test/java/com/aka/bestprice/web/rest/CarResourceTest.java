package com.aka.bestprice.web.rest;

import com.aka.bestprice.Application;
import com.aka.bestprice.domain.Car;
import com.aka.bestprice.repository.CarRepository;
import com.aka.bestprice.repository.search.CarSearchRepository;

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
 * Test class for the CarResource REST controller.
 *
 * @see CarResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CarResourceTest {

    private static final String DEFAULT_VRN = "SAMPLE_TEXT";
    private static final String UPDATED_VRN = "UPDATED_TEXT";
    private static final String DEFAULT_MAKE = "SAMPLE_TEXT";
    private static final String UPDATED_MAKE = "UPDATED_TEXT";
    private static final String DEFAULT_MODEL = "SAMPLE_TEXT";
    private static final String UPDATED_MODEL = "UPDATED_TEXT";
    private static final String DEFAULT_COLOUR = "SAMPLE_TEXT";
    private static final String UPDATED_COLOUR = "UPDATED_TEXT";

    private static final Integer DEFAULT_MILES = 0;
    private static final Integer UPDATED_MILES = 1;

    private static final Integer DEFAULT_AGE = 0;
    private static final Integer UPDATED_AGE = 1;

    @Inject
    private CarRepository carRepository;

    @Inject
    private CarSearchRepository carSearchRepository;

    private MockMvc restCarMockMvc;

    private Car car;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CarResource carResource = new CarResource();
        ReflectionTestUtils.setField(carResource, "carRepository", carRepository);
        ReflectionTestUtils.setField(carResource, "carSearchRepository", carSearchRepository);
        this.restCarMockMvc = MockMvcBuilders.standaloneSetup(carResource).build();
    }

    @Before
    public void initTest() {
        car = new Car();
        car.setVrn(DEFAULT_VRN);
        car.setMake(DEFAULT_MAKE);
        car.setModel(DEFAULT_MODEL);
        car.setColour(DEFAULT_COLOUR);
        car.setMiles(DEFAULT_MILES);
        car.setAge(DEFAULT_AGE);
    }

    @Test
    @Transactional
    public void createCar() throws Exception {
        int databaseSizeBeforeCreate = carRepository.findAll().size();

        // Create the Car
        restCarMockMvc.perform(post("/api/cars")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(car)))
                .andExpect(status().isCreated());

        // Validate the Car in the database
        List<Car> cars = carRepository.findAll();
        assertThat(cars).hasSize(databaseSizeBeforeCreate + 1);
        Car testCar = cars.get(cars.size() - 1);
        assertThat(testCar.getVrn()).isEqualTo(DEFAULT_VRN);
        assertThat(testCar.getMake()).isEqualTo(DEFAULT_MAKE);
        assertThat(testCar.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testCar.getColour()).isEqualTo(DEFAULT_COLOUR);
        assertThat(testCar.getMiles()).isEqualTo(DEFAULT_MILES);
        assertThat(testCar.getAge()).isEqualTo(DEFAULT_AGE);
    }

    @Test
    @Transactional
    public void getAllCars() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the cars
        restCarMockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
                .andExpect(jsonPath("$.[*].vrn").value(hasItem(DEFAULT_VRN.toString())))
                .andExpect(jsonPath("$.[*].make").value(hasItem(DEFAULT_MAKE.toString())))
                .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL.toString())))
                .andExpect(jsonPath("$.[*].colour").value(hasItem(DEFAULT_COLOUR.toString())))
                .andExpect(jsonPath("$.[*].miles").value(hasItem(DEFAULT_MILES)))
                .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)));
    }

    @Test
    @Transactional
    public void getCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get the car
        restCarMockMvc.perform(get("/api/cars/{id}", car.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(car.getId().intValue()))
            .andExpect(jsonPath("$.vrn").value(DEFAULT_VRN.toString()))
            .andExpect(jsonPath("$.make").value(DEFAULT_MAKE.toString()))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL.toString()))
            .andExpect(jsonPath("$.colour").value(DEFAULT_COLOUR.toString()))
            .andExpect(jsonPath("$.miles").value(DEFAULT_MILES))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE));
    }

    @Test
    @Transactional
    public void getNonExistingCar() throws Exception {
        // Get the car
        restCarMockMvc.perform(get("/api/cars/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

		int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Update the car
        car.setVrn(UPDATED_VRN);
        car.setMake(UPDATED_MAKE);
        car.setModel(UPDATED_MODEL);
        car.setColour(UPDATED_COLOUR);
        car.setMiles(UPDATED_MILES);
        car.setAge(UPDATED_AGE);
        restCarMockMvc.perform(put("/api/cars")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(car)))
                .andExpect(status().isOk());

        // Validate the Car in the database
        List<Car> cars = carRepository.findAll();
        assertThat(cars).hasSize(databaseSizeBeforeUpdate);
        Car testCar = cars.get(cars.size() - 1);
        assertThat(testCar.getVrn()).isEqualTo(UPDATED_VRN);
        assertThat(testCar.getMake()).isEqualTo(UPDATED_MAKE);
        assertThat(testCar.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testCar.getColour()).isEqualTo(UPDATED_COLOUR);
        assertThat(testCar.getMiles()).isEqualTo(UPDATED_MILES);
        assertThat(testCar.getAge()).isEqualTo(UPDATED_AGE);
    }

    @Test
    @Transactional
    public void deleteCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

		int databaseSizeBeforeDelete = carRepository.findAll().size();

        // Get the car
        restCarMockMvc.perform(delete("/api/cars/{id}", car.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Car> cars = carRepository.findAll();
        assertThat(cars).hasSize(databaseSizeBeforeDelete - 1);
    }
}
