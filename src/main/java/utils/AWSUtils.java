package utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Location;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class AWSUtils {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();


    public static List<Location> fetchLocationFileFromS3Bucket(String bucketName, String filename) throws IOException {
        LOGGER.info(String.format("Started fetching \"%s\" from S3 bucket \"%s\" ...", filename, bucketName));
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();

        S3Object s3Object = s3.getObject(bucketName, filename);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();

        Location[] locations = new ObjectMapper().readValue(inputStream, Location[].class);

        LOGGER.info("Successfully fetched and parsed " + filename);
        return Arrays.asList(locations);
    }
}
