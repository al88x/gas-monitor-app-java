package service;

import model.GasReading;
import model.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GasReadingService {
    private static final int FIVE_MINUTES = 300000;
    private List<Location> locationList;
    private HashMap<String, Long> processedGasReadings;

    public GasReadingService(List<Location> locationList){
        this.locationList = locationList;
        processedGasReadings = new HashMap<>();
    }

    public boolean isValid(GasReading gasReading) {
        if(isGasReadingFromLocationList(gasReading) && !isDuplicateReading(gasReading)){
            processedGasReadings.put(gasReading.getEventId(), gasReading.getTimestamp());
            return true;
        }
        return false;
    }

    private boolean isDuplicateReading(GasReading gasReading) {
        cleanHashMap(gasReading.getTimestamp());
        for(String readingId : processedGasReadings.keySet()){
            if(gasReading.getEventId().equals(readingId)){
                return true;
            }
        }
        return false;
    }

    private void cleanHashMap(long timestamp) {
        for(Map.Entry<String, Long> entry : processedGasReadings.entrySet()){
            if(timestamp - entry.getValue() > FIVE_MINUTES){
                processedGasReadings.remove(entry.getKey());
            }
        }
    }

    private boolean isGasReadingFromLocationList(GasReading gasReading) {
        return locationList.stream()
                .anyMatch(location -> location.getId().equals(gasReading.getLocationId()));
    }
}
