package com.vadzimvincho.threadService;

import com.vadzimvincho.entity.Car;
import com.vadzimvincho.entity.Parking;
import com.vadzimvincho.entity.ParkingPlace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CarThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(CarThread.class);

    private final Parking parking;
    private final Car car;
    private final Semaphore semaphore;
    private final Exchanger<ParkingPlace> exchanger;
    private ParkingPlace emptyParkingPlace;

    public CarThread(Parking parking, Car car, Semaphore semaphore, Exchanger<ParkingPlace> exchanger) {
        this.parking = parking;
        this.car = car;
        this.semaphore = semaphore;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(car.getName());
        logger.debug("{} created", Thread.currentThread().getName());

        try {
            if (semaphore.tryAcquire(car.getWaitingDuration(), TimeUnit.MILLISECONDS)) {
                emptyParkingPlace = parking.takeParkingPlaces();
            }
            if (emptyParkingPlace != null) {
                logger.debug("{} took parking place №{}", Thread.currentThread().getName(), emptyParkingPlace.getIdNumber());
                parking.incParkedCarCounter();
                emptyParkingPlace = exchangeParkingPlace(emptyParkingPlace);
                Thread.sleep(car.getParkingDuration());
                parking.freeUpParkingPlace(emptyParkingPlace);
                logger.debug("{} leave parking place №{}", Thread.currentThread().getName(), emptyParkingPlace.getIdNumber());
            } else {
                logger.debug("{} has left. The waiting time is over", Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            logger.error("{} interrupted", Thread.currentThread().getName(), e);
        } finally {
            semaphore.release();
        }
    }

    private ParkingPlace exchangeParkingPlace(ParkingPlace emptyParkingPlace) throws InterruptedException {
        Random random = new Random();
        int s = random.nextInt() % 2;
        if (s == 0) {
            try {
                emptyParkingPlace = exchanger.exchange(emptyParkingPlace, 300, TimeUnit.MILLISECONDS);
                logger.debug("{} changed parking place №{}", Thread.currentThread().getName(), emptyParkingPlace.getIdNumber());
            } catch (TimeoutException e) {
                logger.debug("{} cannot change the parking space", Thread.currentThread().getName());
            }
        }
        return emptyParkingPlace;
    }
}
