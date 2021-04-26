package com.vadzimvincho.entity;

public class Car {
    private String name;
    private long parkingDuration;
    private long waitingDuration;

    public Car(String name, long parkingDuration, long waitingDuration) {
        this.name = name;
        this.parkingDuration = parkingDuration;
        this.waitingDuration = waitingDuration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getParkingDuration() {
        return parkingDuration;
    }

    public void setParkingDuration(long parkingDuration) {
        this.parkingDuration = parkingDuration;
    }

    public long getWaitingDuration() {
        return waitingDuration;
    }

    public void setWaitingDuration(long waitingDuration) {
        this.waitingDuration = waitingDuration;
    }
}