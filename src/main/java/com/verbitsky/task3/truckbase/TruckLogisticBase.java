package com.verbitsky.task3.truckbase;

import com.verbitsky.task3.entity.Truck;
import com.verbitsky.task3.state.impl.TruckStateCreation;
import com.verbitsky.task3.state.impl.TruckStateLeaveBase;
import com.verbitsky.task3.state.impl.TruckStateQueueAwait;
import com.verbitsky.task3.truckexception.TruckException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public enum TruckLogisticBase {
    INSTANCE;

    private static final String MAX_PROCESSING_TRUCK_COUNT = "max.processing.truck.count";
    private static final String PROPERTY_FILE = "data/logisticBaseSettings.properties";
    private static Logger logger = LogManager.getLogger();
    private Semaphore semaphore;
    private TruckQueue<Truck> queue;
    private AtomicInteger queueNumber;
    private AtomicInteger processedTrucksCount;
    private boolean isInit = false;

    public void init ()    {
        TruckBasePropertyManager propertyManager = TruckBasePropertyManager.INSTANCE;
        try {
            propertyManager.initManager(PROPERTY_FILE);
        } catch (TruckException e) {
            logger.log(Level.FATAL, "Error property manager initialization");
            throw new RuntimeException("Error property manager initialization", e);
        }
        int maxTruckProcessingCount = propertyManager
                .getIntProperty(MAX_PROCESSING_TRUCK_COUNT)
                .orElseThrow(() -> new RuntimeException("Error to get property 'max.processing.truck.count'"));
        queueNumber = new AtomicInteger(0);
        processedTrucksCount = new AtomicInteger(0);
        queue = new TruckQueue<>();
        semaphore =new Semaphore(maxTruckProcessingCount);
        isInit = true;
    }

    public AtomicInteger getProcessedTrucksCount() {
        return processedTrucksCount;
    }

    public void requestProcessingPermission(Truck truck) throws TruckException {
        if (!isInit) {
            init();
        }
        if (truck == null) {
            throw new TruckException("requestTruckProcessingPermission calling with Null");
        }
        //if truck not null and truck state matching Creation state - add it to queue
        if (truck.getCurrentState() instanceof TruckStateCreation) {
            try {
                truck.setQueueNumber(queueNumber.incrementAndGet());
                queue.offer(truck);
                truck.setState(new TruckStateQueueAwait(truck));
                truck.getTruckLock().lock();
                truck.getWorkingCondition().await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                truck.getTruckLock().unlock();
            }
        } else {
            throw new TruckException("requestTruckProcessingPermission calling with wrong state");
        }
        processQueue();
    }

    public void processQueue() {
        if (!isInit) {
            init();
        }
        //take permission if exist free
        Truck truck = null;
        try {
            if (semaphore.tryAcquire()) {
                truck = queue.poll();
                if (truck != null) {
                    truck.getTruckLock().lock();
                    truck.getWorkingCondition().signal();
                    logger.log(Level.INFO, truck.toString().concat(" got permission to process loading/unloading"));
                }
            } else {
                logger.log(Level.INFO, "Current permissions count = 0, hold truck in queue");
            }
        } finally {
            if (truck != null) {
                truck.getTruckLock().unlock();
            }
        }
    }

    public void releaseProcessingPermission(Truck truck) {
        if (!isInit) {
            init();
        }
        semaphore.release();
        truck.setState(new TruckStateLeaveBase(truck));
        processedTrucksCount.getAndIncrement();
    }
}
