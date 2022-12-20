package com.vadzimvincho;

import com.vadzimvincho.entity.Car;
import com.vadzimvincho.entity.Parking;
import com.vadzimvincho.threadService.CarThreadExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        List<Car> carList = List.of(
                new Car("car#1", 1500, 300),
                new Car("car#2", 1200, 300),
                new Car("car#3", 2000, 500),
                new Car("car#4", 2500, 300),
                new Car("car#5", 3000, 300),
                new Car("car#6", 100, 3000),
                new Car("car#7", 200, 3000),
                new Car("car#8", 100, 3000));

        Parking parking = new Parking(3);

        CarThreadExecutor carThreadExecutor = new CarThreadExecutor(parking, carList);

        carThreadExecutor.carThreadExecute();

        logger.debug("Number of cars using parking: {}", parking.getParkedCarCounter());
    }
}
