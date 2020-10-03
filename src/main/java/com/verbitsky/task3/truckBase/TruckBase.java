package com.verbitsky.task3.truckBase;

import com.verbitsky.task3.entity.Truck;
import com.verbitsky.task3.state.impl.Creation;
import com.verbitsky.task3.state.impl.LeaveBase;
import com.verbitsky.task3.state.impl.TruckQueue;
import com.verbitsky.task3.truckExcpetion.TruckException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public enum TruckBase {
    INSTANCE;
    private static Logger logger = LogManager.getLogger();
    private static final int MAX_TRUCK_PROCESSING_COUNT = 3;
    private static Semaphore semaphore = new Semaphore(MAX_TRUCK_PROCESSING_COUNT);
    private static Queue<Truck> queue = new PriorityQueue<>();
    private static Lock baseLocker = new ReentrantLock();
    private static AtomicInteger queueNumber = new AtomicInteger(0);
    private static AtomicInteger processedTrucksCount = new AtomicInteger(0);


    public static AtomicInteger getProcessedTrucksCount() {
        return processedTrucksCount;
    }

    public void requestProcessingPermission(Truck truck) throws TruckException {
        if (truck == null) {
            throw new TruckException("requestTruckProcessingPermission calling with Null");
        }
        //if truck not null and truck state matching Creation state - add it to queue
        if (truck.getCurrentState() instanceof Creation) {
            try {
                baseLocker.lock();
                truck.setQueueNumber(queueNumber.incrementAndGet());
                queue.offer(truck);
                truck.setState(new TruckQueue(truck));
                truck.getTruckLock().lock();
                truck.getWorkingCondition().await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                baseLocker.unlock();
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
        truck.setState(new LeaveBase(truck));
        processedTrucksCount.getAndIncrement();
    }
}
