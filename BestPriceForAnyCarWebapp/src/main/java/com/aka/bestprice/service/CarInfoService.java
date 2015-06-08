package com.aka.bestprice.service;

import com.aka.bestprice.domain.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CarInfoService {

    private final Logger log = LoggerFactory.getLogger(CarInfoService.class);

    public Car lookup(String vrn)
    {
        Car car = new Car();
        car.setId(123L);
        car.setAge(3);
        car.setColour("Red");
        car.setMake("BMW");
        car.setMiles(10000);
        car.setModel("325 SE");
        car.setVrn(vrn);
        return car;
    }

}
