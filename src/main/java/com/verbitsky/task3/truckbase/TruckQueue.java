package com.verbitsky.task3.truckbase;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TruckQueue <Truck> {
    private Queue<Truck> queue;
    private Lock queueLocker;

    public TruckQueue() {
        this.queue = new PriorityQueue<>();
        queueLocker = new ReentrantLock();
    }

    public boolean offer(Truck truck) {
        boolean res;
        try {
            queueLocker.lock();
            res= queue.offer(truck);
        }finally {
            queueLocker.unlock();
        }
        return res;
    }

    public Truck poll() {
        Truck  res;
        try {
            queueLocker.lock();
            res= queue.poll();
        }finally {
            queueLocker.unlock();
        }
        return res;
    }
}
