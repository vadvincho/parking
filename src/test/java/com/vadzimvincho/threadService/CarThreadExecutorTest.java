package com.vadzimvincho.threadService;

import com.vadzimvincho.entity.Car;
import com.vadzimvincho.entity.Parking;
import com.vadzimvincho.entity.ParkingPlace;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

class CarThreadExecutorTest {
    List<Car> carList;
    Parking parking = new Parking(2);
    ExecutorService threadPool = Executors.newCachedThreadPool();
    Semaphore semaphore;
    Exchanger<ParkingPlace> exchanger = new Exchanger<>();


    @BeforeEach
    void init() {
        carList = List.of(
                new Car("car#1", 1500, 300),
                new Car("car#2", 1200, 300),
                new Car("car#3", 2000, 500),
                new Car("car#4", 2500, 300),
                new Car("car#6", 100, 3000));
        this.semaphore = new Semaphore(2, true);
    }

    @Test
    void car3WaitingTimeIsOver() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future1 = CompletableFuture
                .runAsync(new CarThread(parking, carList.get(0), semaphore, exchanger), threadPool);
        CompletableFuture<Void> future2 = CompletableFuture
                .runAsync(new CarThread(parking, carList.get(1), semaphore, exchanger), threadPool);
        CompletableFuture<Void> future3 = CompletableFuture
                .runAsync(new CarThread(parking, carList.get(2), semaphore, exchanger), threadPool);
        CompletableFuture.allOf(future1, future2, future3).get();
        Assertions.assertTrue(future1.isDone());
        Assertions.assertTrue(future2.isDone());
        Assertions.assertTrue(future3.isDone());
        Assertions.assertEquals(2, parking.getParkedCarCounter().get());
    }

    @Test
    void car5TakeParkingPlace() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future1 = CompletableFuture
                .runAsync(new CarThread(parking, carList.get(0), semaphore, exchanger), threadPool);
        CompletableFuture<Void> future2 = CompletableFuture
                .runAsync(new CarThread(parking, carList.get(1), semaphore, exchanger), threadPool);
        CompletableFuture<Void> future3 = CompletableFuture
                .runAsync(new CarThread(parking, carList.get(4), semaphore, exchanger), threadPool);
        CompletableFuture.allOf(future1, future2, future3).get();
        Assertions.assertTrue(future1.isDone());
        Assertions.assertTrue(future2.isDone());
        Assertions.assertTrue(future3.isDone());
        Assertions.assertEquals(3, parking.getParkedCarCounter().get());
    }

    @Test
    void throwExecutionException() {
        Assertions.assertThrows(ExecutionException.class, () -> CompletableFuture.runAsync(
                new CarThread(null, carList.get(0), semaphore, exchanger), threadPool).get());
    }
}
