package com.verbitsky.task3.state.impl;

import com.verbitsky.task3.entity.Truck;
import com.verbitsky.task3.state.TruckState;
import com.verbitsky.task3.truckbase.TruckLogisticBase;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TruckStateBaseProcessing implements TruckState {
    private static final String BASE_PROCESSING_STATE = "Await truck-base processing";
    private static Logger logger = LogManager.getLogger();

    private final Truck truck;

    public TruckStateBaseProcessing(Truck truck) {
        this.truck = truck;
    }

    @Override
    public void createNewTruck() {
        logger.log(Level.ERROR, "Invoke unsupported operation (create new Truck) in TruckBaseProcessing State");
        throw new UnsupportedOperationException("This operation unsupported with current state");
    }

    @Override
    public void followToTruckQueue() {
        logger.log(Level.ERROR, "Invoke unsupported operation (follow to truck queue) in TruckBaseProcessing State");
        throw new UnsupportedOperationException("This operation unsupported with current state");
    }

    @Override
    public void followToTruckBase() {
        logger.log(Level.ERROR, "Invoke unsupported operation (follow to truck base) in TruckBaseProcessing State");
        throw new UnsupportedOperationException("This operation unsupported with current state");
    }

    @Override
    public void leaveBase() {
        logger.log(Level.INFO, truck.toString().concat(" finished loading/unloading process and leaving base"));
        TruckLogisticBase.INSTANCE.releaseProcessingPermission(truck);
    }

    @Override
    public String toString() {
        return BASE_PROCESSING_STATE;
    }
}