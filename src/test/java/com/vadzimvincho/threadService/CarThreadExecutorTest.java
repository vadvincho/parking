package com.vadzimvincho.threadService;

import com.vadzimvincho.entity.Car;
import com.vadzimvincho.entity.Parking;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.*;

class CarThreadExecutorTest {
    List<Car> carList = new ArrayList<>();
    Parking parking;
    ExecutorService executorService = Executors.newCachedThreadPool();
    Semaphore semaphore;
    ReentrantLock lock;


    @BeforeEach
    void init(){
        Car car1 = new Car("car#1",1500,300);
        Car car2 = new Car("car#2",1200,300);
        Car car3 = new Car("car#3",2000,500);
        Car car4 = new Car("car#4",2500,300);
        Car car5 = new Car("car#5",3000,300);
        Car car6 = new Car("car#6",100,3000);
        Car car7 = new Car("car#7",200,3000);
        Car car8 = new Car("car#8",100,3000);

        carList.add(car1);
        carList.add(car2);
        carList.add(car3);
        carList.add(car4);
        carList.add(car5);
        carList.add(car6);
        carList.add(car7);
        carList.add(car8);

        parking = new Parking(4);
        semaphore = new Semaphore(parking.getParkingPlaces().size(),true);
        lock = new ReentrantLock();
    }

    @Test
    void carThreadExecute() throws ExecutionException, InterruptedException {
        CompletableFuture completableFuture = CompletableFuture
                .runAsync(new CarThread(parking,carList.get(0),semaphore,lock),executorService);
        completableFuture.get();
        Assertions.assertEquals(1, parking.getParkedCarCounter());
    }
}