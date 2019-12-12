package service;

import model.GasReading;
import model.Location;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GasReadingService {
    private static final int FIVE_MINUTES = 300000;
    private static final int MIN = 60000;
    private List<Location> locationList;
    private Map<String, Long> processedGasReadings;
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();


    public GasReadingService(List<Location> locationList){
        this.locationList = locationList;
        processedGasReadings = new HashMap<>();
        
    }

    public boolean processReading(GasReading gasReading) {
        LOGGER.info("Started validating gas reading: " + gasReading.toString());
        if(isGasReadingFromLocationList(gasReading) && !isDuplicateReading(gasReading)){

            processedGasReadings.put(gasReading.getEventId(), gasReading.getTimestamp());
            LOGGER.info("Finished validation. Gas reading is valid: " + gasReading.toString());
            return true;
        }
        LOGGER.info("Finished validation. Gas Reading is invalid: " + gasReading.toString());
        return false;
    }



    private boolean isDuplicateReading(GasReading gasReading) {
        cleanHashMap(gasReading.getTimestamp());
        for(String readingId : processedGasReadings.keySet()){
            if(gasReading.getEventId().equals(readingId)){
                LOGGER.info("Gas reading is a duplicate: " + gasReading.toString());
                return true;
            }
        }
        return false;
    }

    private void cleanHashMap(long timestamp) {
        for(Map.Entry<String, Long> entry : processedGasReadings.entrySet()){
            if(timestamp - entry.getValue() > FIVE_MINUTES){
                LOGGER.info(String.format("Deleting eventKey of a gas reading older than 5 minutes. " +
                                "EventKey: %s, Timestamp: %d", entry.getKey(), entry.getValue()));
                processedGasReadings.remove(entry.getKey());
            }
        }
    }

    private boolean isGasReadingFromLocationList(GasReading gasReading) {
        boolean isInLocationList = locationList.stream()
                .anyMatch(location -> location.getId().equals(gasReading.getLocationId()));
        if(!isInLocationList){
            LOGGER.info("Unknown location for gas reading: " + gasReading.toString());
        }
        return isInLocationList;
    }
}
