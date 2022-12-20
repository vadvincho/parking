package com.vadzimvincho.threadService;

import com.vadzimvincho.entity.Car;
import com.vadzimvincho.entity.Parking;
import com.vadzimvincho.entity.ParkingPlace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CarThreadExecutor {
    private static final Logger logger = LoggerFactory.getLogger(CarThreadExecutor.class);

    private final Parking parking;
    private final List<Car> cars;
    private final Semaphore semaphore;
    private final Exchanger<ParkingPlace> exchanger;
    private final ExecutorService executorService;

    public CarThreadExecutor(Parking parking, List<Car> cars) {
        this.parking = parking;
        this.cars = cars;
        this.semaphore = new Semaphore(parking.getParkingPlaces().size(), true);
        this.exchanger = new Exchanger<>();
        this.executorService = Executors.newCachedThreadPool();
    }

    public void carThreadExecute() {
        for (Car car : cars) {
            CarThread carThread = new CarThread(parking, car, semaphore, exchanger);
            executorService.execute(carThread);
        }
        executorService.shutdown();

        try {
            Boolean isDone = executorService.awaitTermination(1L, TimeUnit.MINUTES);
            logger.debug("All threads are terminated: {}", isDone);
        } catch (InterruptedException e) {
            logger.error("{} interrupted", Thread.currentThread().getName(), e);
        }
    }
}
