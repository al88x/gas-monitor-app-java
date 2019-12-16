import service.GasReadingService;
import com.amazonaws.services.sqs.model.Message;
import model.Location;
import utils.AWSQueue;
import utils.AWSUtils;
import utils.JSONFileParser;

import java.io.IOException;
import java.time.Clock;
import java.util.List;
import java.util.Objects;


public class Main {

    public static void main(String[] args) throws IOException {
        List<Location> locations = AWSUtils.fetchLocationFileFromS3Bucket(args[0], args[1]);
        GasReadingService gasReadingService = new GasReadingService(locations);

        try (AWSQueue queue = new AWSQueue("gas-reading-data", args[2])) {
            long timeNow = System.currentTimeMillis();
            while (System.currentTimeMillis() < timeNow + 15000) {
                queue.getMessageFromSQSAndDeleteFromQueue().stream()
                        .map(Message::getBody)
                        .map(JSONFileParser::parseSqsMessage)
                        .filter(Objects::nonNull)
                        .filter(gasReadingService::isValidLocation)
                        .filter(gasReadingService::isUniqueReading)
                        .map(gasReadingService::processReadingAndReturnReadingValue)
                        .forEach(reading -> System.out.println("Gas reading: " + reading));
            }
        }
    }
}


