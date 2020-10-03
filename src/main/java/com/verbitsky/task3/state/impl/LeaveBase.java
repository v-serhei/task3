package com.verbitsky.task3.state.impl;

import com.verbitsky.task3.entity.Truck;
import com.verbitsky.task3.state.TruckState;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeaveBase implements TruckState {
    private static final String LEAVING_BASE_STATE = "Left truck-base";
    private static Logger logger = LogManager.getLogger();
    private final Truck truck;

    public LeaveBase(Truck truck) {
        this.truck = truck;
    }

    @Override
    public void createNewTruck() {
        logger.log(Level.ERROR, "Invoke unsupported operation (create new Truck) in LeaveBase State");
        throw new UnsupportedOperationException("This operation unsupported with current state");
    }

    @Override
    public void followToTruckQueue() {
        logger.log(Level.ERROR, "Invoke unsupported operation (follow to truck queue) in LeaveBase State");
        throw new UnsupportedOperationException("This operation unsupported with current state");
    }

    @Override
    public void followToTruckBase() {
        logger.log(Level.ERROR, "Invoke unsupported operation (follow to truck base) in LeaveBase State");
        throw new UnsupportedOperationException("This operation unsupported with current state");
    }

    @Override
    public void leaveBase() {
        logger.log(Level.ERROR, "Invoke unsupported operation (leave base) in LeaveBase State");
        throw new UnsupportedOperationException("This operation unsupported with current state");
    }

    @Override
    public String toString() {
        return LEAVING_BASE_STATE;
    }
}
