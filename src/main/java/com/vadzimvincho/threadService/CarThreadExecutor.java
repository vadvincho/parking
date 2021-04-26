package com.vadzimvincho.threadService;

import com.vadzimvincho.entity.Car;
import com.vadzimvincho.entity.Parking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class CarThreadExecutor {
    private static final Logger logger = LoggerFactory.getLogger(CarThreadExecutor.class);

    public void carThreadExecute(Parking parking, List<Car> cars) {
        ReentrantLock locker = new ReentrantLock();
        Semaphore semaphore = new Semaphore(parking.getParkingPlaces().size(), true);
        ExecutorService executorService = Executors.newCachedThreadPool();

        cars.stream().map(car -> new CarThread(parking, car, semaphore, locker)).forEach(executorService::execute);
        executorService.shutdown();

        try {
            executorService.awaitTermination(1L, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
}