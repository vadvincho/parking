package com.vadzimvincho.entity;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Parking {
    private final AtomicInteger parkedCarCounter = new AtomicInteger();
    private Queue<ParkingPlace> parkingPlaces = new ConcurrentLinkedQueue<>();

    public Parking(int numberParkingPlaces) {
        IntStream.range(0, numberParkingPlaces)
                .mapToObj(ParkingPlace::new)
                .forEach(parkingPlace -> parkingPlaces.add(parkingPlace));
    }

    public Queue<ParkingPlace> getParkingPlaces() {
        return parkingPlaces;
    }

    public void setParkingPlaces(Queue<ParkingPlace> parkingPlaces) {
        this.parkingPlaces = parkingPlaces;
    }

    public AtomicInteger getParkedCarCounter() {
        return parkedCarCounter;
    }

    public void incParkedCarCounter() {
        parkedCarCounter.incrementAndGet();
    }

    public ParkingPlace takeParkingPlaces() {
        return getParkingPlaces().poll();
    }

    public void freeUpParkingPlace(ParkingPlace parkingPlace) {
        parkingPlaces.add(parkingPlace);
    }
}
