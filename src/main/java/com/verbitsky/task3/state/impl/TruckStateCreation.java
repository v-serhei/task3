package com.verbitsky.task3.state.impl;

import com.verbitsky.task3.entity.Truck;
import com.verbitsky.task3.state.TruckState;
import com.verbitsky.task3.truckbase.TruckLogisticBase;
import com.verbitsky.task3.truckexception.TruckException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TruckStateCreation implements TruckState {
    private static final String CREATION_STATE = "Creation";
    private static Logger logger = LogManager.getLogger();
    private static final String TRUCK_CREATION_MESSAGE = "Create new truck: ";
    private final Truck truck;

    public TruckStateCreation(Truck truck) {
        this.truck = truck;
    }

    @Override
    public void createNewTruck() {
        logger.log(Level.INFO, TRUCK_CREATION_MESSAGE.concat(truck.toString()));
    }

    @Override
    public void followToTruckQueue() throws TruckException {
        logger.log(Level.INFO, truck.toString().concat(" arrived to terminal queue"));
        TruckLogisticBase.INSTANCE.requestProcessingPermission(truck);
    }


    @Override
    public void followToTruckBase() {
        logger.log(Level.ERROR, "Invoke unsupported operation (follow to truck base) in Creation State");
        throw new UnsupportedOperationException("This operation unsupported with current state");
    }

    @Override
    public void leaveBase() {
        logger.log(Level.ERROR, "Invoke unsupported operation (leave base) in Creation State");
        throw new UnsupportedOperationException("This operation unsupported with current state");
    }

    @Override
    public String toString() {
        return CREATION_STATE;
    }
}
