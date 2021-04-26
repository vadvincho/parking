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

class CarThreadExecutorTest {
    List<Car> carList = new ArrayList<>();
    Parking parking;
    ExecutorService threadPool = Executors.newCachedThreadPool();
    Semaphore semaphore;
    ReentrantLock lock;
    Exchanger exchanger;


    @BeforeEach
    void init() {
        Car car1 = new Car("car#1", 1500, 300);
        Car car2 = new Car("car#2", 1200, 300);
        Car car3 = new Car("car#3", 2000, 500);
        Car car4 = new Car("car#4", 2500, 300);
        Car car6 = new Car("car#6", 100, 3000);

        carList.add(car1);
        carList.add(car2);
        carList.add(car3);
        carList.add(car4);
        carList.add(car6);

        parking = new Parking(2);
        semaphore = new Semaphore(2, true);
        lock = new ReentrantLock();
        exchanger = new Exchanger();
    }

    @Test
    void car3WaitingTimeIsOver() throws ExecutionException, InterruptedException {
        CompletableFuture future1 = CompletableFuture
                .runAsync(new CarThread(parking, carList.get(0), semaphore, lock, exchanger), threadPool);
        CompletableFuture future2 = CompletableFuture
                .runAsync(new CarThread(parking, carList.get(1), semaphore, lock, exchanger), threadPool);
        CompletableFuture future3 = CompletableFuture
                .runAsync(new CarThread(parking, carList.get(2), semaphore, lock, exchanger), threadPool);
        CompletableFuture.allOf(future1, future2, future3).get();
            Assertions.assertTrue(future1.isDone());
            Assertions.assertTrue(future2.isDone());
            Assertions.assertTrue(future3.isDone());
            Assertions.assertEquals(2, parking.getParkedCarCounter());
    }

    @Test
    void car6TakeParkingPlace() throws ExecutionException, InterruptedException {
        CompletableFuture future1 = CompletableFuture
                .runAsync(new CarThread(parking, carList.get(0), semaphore, lock, exchanger), threadPool);
        CompletableFuture future2 = CompletableFuture
                .runAsync(new CarThread(parking, carList.get(1), semaphore, lock, exchanger), threadPool);
        CompletableFuture future3 = CompletableFuture
                .runAsync(new CarThread(parking, carList.get(6), semaphore, lock, exchanger), threadPool);
        CompletableFuture.allOf(future1, future2, future3).get();
            Assertions.assertTrue(future1.isDone());
            Assertions.assertTrue(future2.isDone());
            Assertions.assertTrue(future3.isDone());
            Assertions.assertEquals(3, parking.getParkedCarCounter());
    }

    @Test
    void throwExecutionException() {
        Assertions.assertThrows(ExecutionException.class, () -> CompletableFuture.runAsync(
                new CarThread(null, carList.get(0), semaphore, lock, exchanger), threadPool).get());
    }


}