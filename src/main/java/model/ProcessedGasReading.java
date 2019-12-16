package model;

import java.util.Objects;

public class ProcessedGasReading {

    private String eventId;
    private Long timestamp;


    public ProcessedGasReading(String eventId, Long timestamp) {
        this.eventId = eventId;
        this.timestamp = timestamp;
    }

    public String getEventId() {
        return eventId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcessedGasReading)) return false;
        ProcessedGasReading that = (ProcessedGasReading) o;
        return Objects.equals(getEventId(), that.getEventId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEventId());
    }
}
