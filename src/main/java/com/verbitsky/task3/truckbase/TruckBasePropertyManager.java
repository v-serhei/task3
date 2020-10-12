package com.verbitsky.task3.truckbase;

import com.sun.javafx.fxml.PropertyNotFoundException;
import com.verbitsky.task3.truckexception.TruckException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public enum TruckBasePropertyManager {
    INSTANCE;
    private final Logger logger = LogManager.getLogger();
    private Properties properties;

    {
        properties = new Properties();
    }

    public void initManager(String propertyFile) throws TruckException {
        if (propertyFile == null || propertyFile.isEmpty()) {
            logger.log(Level.FATAL, "Error to load properties - wrong property file name");
            throw new TruckException("Error to load properties - wrong property file name");
        }
        logger.log(Level.INFO, "init property manager");
        properties = new Properties();
        try (InputStream propertyFileInputStream = getClass().getClassLoader().getResourceAsStream(propertyFile)) {
            if (propertyFileInputStream == null) {
                logger.log(Level.FATAL, "Error to load properties - property file not found");
                throw new TruckException("Error to load properties - property file not found");
            }
            properties.load(propertyFileInputStream);
        } catch (IOException e) {
            logger.log(Level.FATAL, "Error to load connection properties", e);
            throw new TruckException("Error to load connection properties", e);
        }
    }

    public Optional<Integer> getIntProperty(String propertyName) {
        String extracted;
        Optional<Integer> property;
        try {
            extracted = properties.getProperty(propertyName);
        } catch (PropertyNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Requested property <");
            sb.append(propertyName);
            sb.append("> not found");
            logger.log(Level.WARN, sb.toString());
            property = Optional.empty();
            return property;
        }
        try {
            int value = Integer.parseInt(extracted);
            property = Optional.of(value);
        } catch (NumberFormatException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Requested property <");
            sb.append(propertyName);
            sb.append("> is not an Integer value");
            logger.log(Level.WARN, sb.toString());
            property = Optional.empty();
        }
        return property;
    }
}