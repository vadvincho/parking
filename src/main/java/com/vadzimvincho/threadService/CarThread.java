package com.vadzimvincho.threadService;

import com.vadzimvincho.entity.Car;
import com.vadzimvincho.entity.Parking;
import com.vadzimvincho.entity.ParkingPlace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class CarThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(CarThread.class);

    private Parking parking;
    private Car car;
    private Semaphore semaphore;
    private ReentrantLock locker;

    public CarThread(Parking parking, Car car, Semaphore semaphore, ReentrantLock locker) {
        this.parking = parking;
        this.car = car;
        this.semaphore = semaphore;
        this.locker = locker;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(car.getName());
        logger.debug(Thread.currentThread().getName() + " created");

        ParkingPlace emptyParkingPlace = null;
        try {
            if (semaphore.tryAcquire(car.getWaitingDuration(), TimeUnit.MILLISECONDS)) {
                emptyParkingPlace = parking.takeParkingPlaces();
            }
            if (emptyParkingPlace != null) {
                logger.debug(Thread.currentThread().getName() + " took parking place №" + emptyParkingPlace.getIdNumber());
                locker.lock();
                parking.incParkedCarCounter();
                locker.unlock();
                Thread.sleep(car.getParkingDuration());
                parking.freeUpParkingPlace(emptyParkingPlace);
                logger.debug(Thread.currentThread().getName() + " leave parking place №" + emptyParkingPlace.getIdNumber());
            } else {
                logger.debug(Thread.currentThread().getName() + " has left. The waiting time is over");
            }
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        } finally {
            semaphore.release();
        }
    }
}