package com.vadzimvincho.entity;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Parking {
    private int parkedCarCounter;
    private Queue<ParkingPlace> parkingPlaces = new ConcurrentLinkedQueue<>();

    public Parking(int numberParkingPlaces) {
        for (int i = 0; i < numberParkingPlaces; i++) {
            ParkingPlace parkingPlace = new ParkingPlace();
            parkingPlace.setIdNumber(i);
            parkingPlaces.add(parkingPlace);
        }
    }

    public Queue<ParkingPlace> getParkingPlaces() {
        return parkingPlaces;
    }

    public void setParkingPlaces(Queue<ParkingPlace> parkingPlaces) {
        this.parkingPlaces = parkingPlaces;
    }

    public int getParkedCarCounter() {
        return parkedCarCounter;
    }

    public void incParkedCarCounter() {
        parkedCarCounter++;
    }

    public ParkingPlace takeParkingPlaces() {
        return getParkingPlaces().poll();
    }

    public void freeUpParkingPlace(ParkingPlace parkingPlace) {
        parkingPlaces.add(parkingPlace);
    }
}
