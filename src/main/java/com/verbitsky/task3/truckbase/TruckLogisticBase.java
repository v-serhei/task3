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
    private static Logger logger = LogManager.getLogger();
    private static final int MAX_TRUCK_PROCESSING_COUNT = 3;
    private Semaphore semaphore = new Semaphore(MAX_TRUCK_PROCESSING_COUNT);
    private TruckQueue<Truck> queue = new TruckQueue<>();
    private AtomicInteger queueNumber = new AtomicInteger(0);
    private AtomicInteger processedTrucksCount = new AtomicInteger(0);


    public AtomicInteger getProcessedTrucksCount() {
        return processedTrucksCount;
    }

    public void requestProcessingPermission(Truck truck) throws TruckException {
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
        //взять разрешение а если нет - слипнуть поток
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
        semaphore.release();
        truck.setState(new TruckStateLeaveBase(truck));
        processedTrucksCount.getAndIncrement();
    }
}
