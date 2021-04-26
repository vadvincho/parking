package com.vadzimvincho;

import com.vadzimvincho.entity.Car;
import com.vadzimvincho.threadService.CarThreadExecutor;
import com.vadzimvincho.entity.Parking;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Car car1 = new Car("car#1",1500,300);
        Car car2 = new Car("car#2",1200,300);
        Car car3 = new Car("car#3",2000,500);
        Car car4 = new Car("car#4",2500,300);
        Car car5 = new Car("car#5",3000,300);
        Car car6 = new Car("car#6",100,3000);
        Car car7 = new Car("car#7",200,3000);
        Car car8 = new Car("car#8",100,3000);

        List<Car> carList = new ArrayList<>();

        carList.add(car1);
        carList.add(car2);
        carList.add(car3);
        carList.add(car4);
        carList.add(car5);
        carList.add(car6);
        carList.add(car7);
        carList.add(car8);

        Parking parking = new Parking(3);

        CarThreadExecutor carThreadExecutor = new CarThreadExecutor();
        carThreadExecutor.carThreadExecute(parking,carList);

        System.out.println(parking.getParkedCarCounter());
    }
}
