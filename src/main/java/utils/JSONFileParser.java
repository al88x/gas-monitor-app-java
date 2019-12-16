package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import model.GasReading;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;

public class JSONFileParser {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();


    public static GasReading parseSqsMessage(String sqsMessage) {

        JsonObject jsonObject = JsonParser.parseString(sqsMessage).getAsJsonObject();
        String gasReadingFromSqs = jsonObject.get("Message").getAsString();

        ObjectMapper mapper = new ObjectMapper();
        GasReading reading = null;
        try {
            reading = mapper.readValue(gasReadingFromSqs, GasReading.class);
        } catch (IOException e) {
            return null;
        }
        if(reading.hasMissingField()){
            LOGGER.info("Incomplete gas reading: " + reading.toString());
            return null;
        }
        return reading;
    }

}
