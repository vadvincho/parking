package com.vadzimvincho.threadService;

import com.vadzimvincho.entity.Car;
import com.vadzimvincho.entity.Parking;
import com.vadzimvincho.entity.ParkingPlace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class CarThreadExecutor {
    private static final Logger logger = LoggerFactory.getLogger(CarThreadExecutor.class);

    public void carThreadExecute(Parking parking, List<Car> cars) {
        ReentrantLock locker = new ReentrantLock();
        Semaphore semaphore = new Semaphore(parking.getParkingPlaces().size(), true);
        Exchanger<ParkingPlace> exchanger = new Exchanger<>();
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (Car car : cars) {
            CarThread carThread = new CarThread(parking, car, semaphore, locker, exchanger);
            executorService.execute(carThread);
        }
        executorService.shutdown();

        try {
            executorService.awaitTermination(1L, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
}