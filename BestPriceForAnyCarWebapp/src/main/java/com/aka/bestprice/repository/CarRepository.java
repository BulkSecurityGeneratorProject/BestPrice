package com.aka.bestprice.repository;

import com.aka.bestprice.domain.Car;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Car entity.
 */
public interface CarRepository extends JpaRepository<Car,Long> {

}
