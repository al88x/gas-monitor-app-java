package model;

public class GasReading {
    private String locationId;
    private String eventId;
    private double value;
    private long timestamp;

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "GasReading{" +
                "locationId='" + locationId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", value=" + value +
                ", timestamp=" + timestamp +
                '}';
    }
}
