package com.verbitsky.task3.state;

import com.verbitsky.task3.truckExcpetion.TruckException;

public interface TruckState {
    void createNewTruck();

    void followToTruckQueue() throws TruckException, InterruptedException;

    void followToTruckBase() throws TruckException;

    void leaveBase();
}
