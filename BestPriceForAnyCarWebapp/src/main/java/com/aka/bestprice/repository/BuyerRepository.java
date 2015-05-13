package com.aka.bestprice.repository;

import com.aka.bestprice.domain.Buyer;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Buyer entity.
 */
public interface BuyerRepository extends JpaRepository<Buyer,Long> {

}
