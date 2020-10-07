package com.verbitsky.task3.truckcreator;

import com.verbitsky.task3.entity.Truck;
import com.verbitsky.task3.entity.TruckType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TruckCreator {
    private static final Random rnd = new Random();
    private static final String TRUCK_PREFIX_NAME= "Truck#";

    private TruckCreator() {
    }

    public static List<Truck> createRandomTypeTrucks(int count) {
        List<Truck> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            TruckType truckType = getRandomType();
            StringBuilder name = new StringBuilder(TRUCK_PREFIX_NAME);
            name.append(i+1);
            list.add(new Truck(name.toString(), truckType));
        }
        return list;
    }

    private static TruckType getRandomType() {
        TruckType truckType;
        int i = rnd.nextInt(2);
        switch (i) {
            case 0: {
                truckType = TruckType.COMMON;
                break;
            }
            case 1: {
                truckType = TruckType.SPECIAL;
                break;
            }
            default: {
                truckType = TruckType.COMMON;
            }
        }
        return truckType;
    }
}
