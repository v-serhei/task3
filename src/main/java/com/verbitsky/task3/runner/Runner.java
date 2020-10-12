package com.verbitsky.task3.runner;

import com.verbitsky.task3.entity.Truck;
import com.verbitsky.task3.truckbase.TruckBasePropertyManager;
import com.verbitsky.task3.truckbase.TruckLogisticBase;
import com.verbitsky.task3.truckcreator.TruckCreator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Runner {
    private static final String TRUCK_COUNT = "truck.count";
    private static final String AWAIT_TIME = "await.time";
    private static final String PROCESSING_INTERVAL = "processing.interval";
    private static Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        TruckLogisticBase.INSTANCE.init();
        TruckBasePropertyManager propertyManager = TruckBasePropertyManager.INSTANCE;
        int truckCount = propertyManager
                .getIntProperty(TRUCK_COUNT)
                .orElseThrow(() -> new RuntimeException("Error to get property 'truck.count'"));
        int awaitTime = propertyManager
                .getIntProperty(AWAIT_TIME)
                .orElseThrow(() -> new RuntimeException("Error to get property 'await.time'"));
        int processingInterval = propertyManager
                .getIntProperty(PROCESSING_INTERVAL)
                .orElseThrow(() -> new RuntimeException("Error to get property 'processing.interval'"));

        logger.log(Level.INFO, "Start emulation\n\n");
        List<Truck> truckList = TruckCreator.createRandomTypeTrucks(truckCount);
        truckList.forEach(Thread::start);
        while (TruckLogisticBase.INSTANCE.getProcessedTrucksCount().get() < truckCount) {
            try {
                TimeUnit.MILLISECONDS.sleep(processingInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TruckLogisticBase.INSTANCE.processQueue();
        }
        try {
            TimeUnit.MILLISECONDS.sleep(awaitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.log(Level.INFO, "\n\nFinish emulation");
    }
}
