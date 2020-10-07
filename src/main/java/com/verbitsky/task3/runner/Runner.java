package com.verbitsky.task3.runner;

import com.verbitsky.task3.entity.Truck;
import com.verbitsky.task3.truckbase.TruckLogisticBase;
import com.verbitsky.task3.truckcreator.TruckCreator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Runner {
    private static final int TRUCK_COUNT = 15;
    private static final int DEFAULT_AWAIT_TIME = 1;
    public static final int DEFAULT_PROCESSING_INTERVAL = 300;
    private static Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.log(Level.INFO, "Start emulation\n\n");
        List<Truck> truckList = TruckCreator.createRandomTypeTrucks(TRUCK_COUNT);
        truckList.forEach(Thread::start);
        while (TruckLogisticBase.INSTANCE.getProcessedTrucksCount().get() < TRUCK_COUNT) {
            try {
                TimeUnit.MILLISECONDS.sleep(DEFAULT_PROCESSING_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TruckLogisticBase.INSTANCE.processQueue();
        }
        try {
            TimeUnit.MILLISECONDS.sleep(DEFAULT_AWAIT_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.log(Level.INFO, "\n\nFinish emulation");

    }
}
