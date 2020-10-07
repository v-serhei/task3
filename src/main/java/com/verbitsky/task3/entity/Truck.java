package com.verbitsky.task3.entity;

import com.verbitsky.task3.state.TruckState;
import com.verbitsky.task3.state.impl.TruckStateCreation;
import com.verbitsky.task3.truckexception.TruckException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Truck extends Thread implements Comparable<Truck> {
    private static Logger logger = LogManager.getLogger();
    private String truckName;
    private TruckType type;
    private TruckState currentState;
    private int queueNumber;
    private Lock truckLock;
    private Condition working;

    public Truck(String name, TruckType type) {
        setTruckName(name);
        setType(type);
        currentState = new TruckStateCreation(this);
        currentState.createNewTruck();
        queueNumber = 0;
        truckLock = new ReentrantLock();
        working = truckLock.newCondition();
    }

    public Condition getWorkingCondition() {
        return working;
    }

    public Lock getTruckLock() {
        return truckLock;
    }

    public int getQueueNumber() {
        return queueNumber;
    }

    public void setQueueNumber(int queueNumber) {
        this.queueNumber = queueNumber;
    }

    public TruckType getType() {
        return type;
    }

    public void setType(TruckType type) {
        this.type = type;
    }

    public TruckState getCurrentState() {
        return currentState;
    }

    public void setState(TruckState currentState) {
        this.currentState = currentState;
        StringBuilder sb = new StringBuilder();
        sb.append(this.getTruckName());
        sb.append(" changed State on <");
        sb.append(getCurrentState());
        sb.append(">");
        logger.log(Level.INFO, this.toString().concat(sb.toString()));
    }

    public String getTruckName() {
        return truckName;
    }

    public void setTruckName(String truckName) {
        this.truckName = truckName;
    }

    @Override
    public void run() {
        try {
            //state = creation
            currentState.followToTruckQueue();
            //state = TruckQueue
            currentState.followToTruckBase();
            //State = TruckBaseProcessing
            currentState.leaveBase();
            //last State here - Leave base
        } catch (TruckException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Truck)) return false;

        Truck truck = (Truck) o;

        if (truckName != null ? !truckName.equals(truck.truckName) : truck.truckName != null) return false;
        if (type != truck.type) return false;
        return currentState != null ? currentState.equals(truck.currentState) : truck.currentState == null;
    }

    @Override
    public int hashCode() {
        int result = truckName != null ? truckName.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (currentState != null ? currentState.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Truck [name=");
        sb.append(getTruckName());
        sb.append(", type=");
        sb.append(getType());
        sb.append(", current state=");
        sb.append(getCurrentState());
        sb.append(", current Thread State=");
        sb.append(this.getState());
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int compareTo(Truck o) {
        if (this.getType() == TruckType.SPECIAL & o.getType() == TruckType.SPECIAL) {
            return Integer.compare(this.getQueueNumber(), o.getQueueNumber());
        }
        if (this.getType() == TruckType.COMMON & o.getType() == TruckType.COMMON) {
            return Integer.compare(this.getQueueNumber(), o.getQueueNumber());
        }
        if (this.getType() == TruckType.SPECIAL & o.getType() == TruckType.COMMON) {
            return -1;
        }
        if (this.getType() == TruckType.COMMON & o.getType() == TruckType.SPECIAL) {
            return 1;
        }
    return 0;
    }
}
