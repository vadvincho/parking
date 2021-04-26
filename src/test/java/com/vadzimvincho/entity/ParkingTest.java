package com.vadzimvincho.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ParkingTest {
    Parking parking = new Parking(4);

    @Test
    void incParkedCarCounter() {
        parking.incParkedCarCounter();
        Assertions.assertEquals(1, parking.getParkedCarCounter());
    }

    @Test
    void takeParkingPlaces() {
        ParkingPlace parkingPlace = parking.takeParkingPlaces();
        Assertions.assertTrue(parkingPlace != null);
        Assertions.assertEquals(3, parking.getParkingPlaces().size());
    }

    @Test
    void freeUpParkingPlace() {
        ParkingPlace parkingPlace = new ParkingPlace(5);
        parking.freeUpParkingPlace(parkingPlace);
        Assertions.assertEquals(5, parking.getParkingPlaces().size());
    }
}