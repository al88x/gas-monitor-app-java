package utils;

import com.amazonaws.services.sqs.model.Message;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.GasReading;
import model.Location;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

public class JSONFileParser {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();

    public static List<Location> parseLocationFile(File file) throws FileNotFoundException {
        LOGGER.info("Started parsing json file: " + file.getName());

        Type locationType = new TypeToken<List<Location>>() {
        }.getType();
        Gson gson = new Gson();
        JsonReader reader = null;
        List<Location> locations = null;
        try {
            reader = new JsonReader(new FileReader(file));
            locations = gson.fromJson(reader, locationType);
        } catch (FileNotFoundException e) {
            LOGGER.debug(String.format("File: %s not found at filepath: %s", file.getName(), file.getAbsolutePath()));
            throw e;
        } catch (JsonIOException | JsonSyntaxException e){
            LOGGER.debug(String.format("Failed to parse json from file: %s, filepath: %s", file.getName(), file.getAbsolutePath()));
            throw e;
        }
        LOGGER.info("Successfully parsed json file: " + file.getName());
        return locations;
    }

    public static GasReading parseSqsMessage(String sqsMessage) {
        JsonObject jsonObject = JsonParser.parseString(sqsMessage).getAsJsonObject();
        String gasReadingFromSqs = jsonObject.get("Message").getAsString();
        Gson gson = new Gson();

        return gson.fromJson(gasReadingFromSqs, GasReading.class);
    }

}
