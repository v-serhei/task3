package com.verbitsky.task3.state.impl;

import com.verbitsky.task3.entity.Truck;
import com.verbitsky.task3.state.TruckState;
import com.verbitsky.task3.util.ProcessEmulator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TruckStateQueueAwait implements TruckState {
    private static final String QUEUE_STATE = "awaitTerminalQueue";
    private static Logger logger = LogManager.getLogger();

    private final Truck truck;

    public TruckStateQueueAwait(Truck truck) {
        this.truck = truck;
    }

    @Override
    public void createNewTruck() {
        logger.log(Level.FATAL, "Invoke unsupported operation (create new Truck) in TruckQueue State");
        throw new UnsupportedOperationException("This operation unsupported with current state");
    }

    @Override
    public void followToTruckQueue() {
        logger.log(Level.FATAL, "Invoke unsupported operation (follow to truck queue) in TruckQueue State");
        throw new UnsupportedOperationException("This operation unsupported with current state");
    }


    @Override
    public void followToTruckBase() {
        logger.log(Level.INFO, truck.toString().concat(" got permission to loading/unloading process"));
        truck.setState(new TruckStateBaseProcessing(truck));
        logger.log(Level.INFO, truck.toString().concat(" is in loading/unloading process"));
        //emulate loading/unloading process
        ProcessEmulator.emulateProcessingOperation();
    }

    @Override
    public void leaveBase() {
        logger.log(Level.ERROR, "Invoke unsupported operation (leave base) in TruckQueue State");
        throw new UnsupportedOperationException("This operation unsupported with current state");
    }

    @Override
    public String toString() {
        return QUEUE_STATE;
    }
}
