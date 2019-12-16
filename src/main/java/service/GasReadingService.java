package service;

import model.GasReading;
import model.Location;
import model.ProcessedGasReading;
import org.apache.logging.log4j.LogManager;

import java.time.Clock;
import java.util.*;

public class GasReadingService {
    private static final int FIVE_MINUTES = 300000;
    private List<Location> locationList;
    private Queue<ProcessedGasReading> processedGasReadings;
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();


    public GasReadingService(List<Location> locationList){
        this.locationList = locationList;
        processedGasReadings = new LinkedList<>();

    }

    public double processReadingAndReturnReadingValue(GasReading gasReading) {
            processedGasReadings.add(new ProcessedGasReading(gasReading.getEventId(), Clock.systemDefaultZone().millis()));
            LOGGER.info("Successfully processed Gas Reading: " + gasReading.toString());
            return gasReading.getValue();

    }

    private void cleanQueueOfOldReadings() {
        long now = Clock.systemDefaultZone().millis();
        while(!processedGasReadings.isEmpty() && now - processedGasReadings.peek().getTimestamp() > FIVE_MINUTES){
            processedGasReadings.remove();
        }
    }

    public boolean isValidLocation(GasReading gasReading) {
        boolean isInLocationList = locationList.stream()
                .anyMatch(location -> location.getId().equals(gasReading.getLocationId()));
        if(!isInLocationList){
            LOGGER.info("Failed to process Gas Reading. Unknown location: " + gasReading.toString());
        }
        return isInLocationList;
    }

    public boolean isUniqueReading(GasReading gasReading) {
        cleanQueueOfOldReadings();
        if (processedGasReadings.contains(new ProcessedGasReading(gasReading.getEventId(),null))) {
            LOGGER.info("Failed to process. Gas reading is a duplicate: " + gasReading.toString());
            return false;
        }
        return true;
    }

    public Queue<ProcessedGasReading> getProcessedGasReadings() {
        return processedGasReadings;
    }
}
