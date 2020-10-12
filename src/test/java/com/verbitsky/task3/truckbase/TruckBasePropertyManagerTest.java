package com.verbitsky.task3.truckbase;

import com.verbitsky.task3.truckexception.TruckException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Optional;

public class TruckBasePropertyManagerTest {
    private static final String PROPERTY_NAME = "truck.count";
    private static final String WRONG_PROP = "wrong.prop";
    private TruckBasePropertyManager manager;
    private String propertyFile;
    @BeforeClass
    public void init() {
        manager = TruckBasePropertyManager.INSTANCE;
        propertyFile = "data/logisticBaseSettings.properties";
    }
    @AfterClass
    public void tearDown() {
        manager = null;
        propertyFile = null;
    }

    @Test
    public void testGetIntPropertyPositive() throws TruckException {
        manager.initManager(propertyFile);
        int expected = 15;
        Optional <Integer>value = manager.getIntProperty(PROPERTY_NAME);
        int actual = value.get();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetIntPropertyNegativeWrongPropertyName() throws TruckException {
        manager.initManager(propertyFile);
        Optional <Integer> expected = Optional.empty();
        Optional <Integer>actual = manager.getIntProperty(WRONG_PROP);
        Assert.assertEquals(actual, expected);
    }

    @Test (expectedExceptions = TruckException.class)
    public void testInitManagerNullParameter() throws TruckException {
        manager.initManager(null);
    }
}