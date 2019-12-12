package utils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AWSUtils {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();


    public static File fetchLocationFileFromS3Bucket(String bucketName, String filename){
            LOGGER.info(String.format("Started downloading \"%s\" from S3 bucket \"%s\" ...", filename, bucketName));
            final AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();
            String path = "src/main/java/locations/" + filename;
            try {
                S3Object o = s3.getObject(bucketName, filename);
                S3ObjectInputStream s3is = o.getObjectContent();
                FileOutputStream fos = new FileOutputStream(new File(path));
                byte[] read_buf = new byte[1024];
                int read_len = 0;
                while ((read_len = s3is.read(read_buf)) > 0) {
                    fos.write(read_buf, 0, read_len);
                }
                s3is.close();
                fos.close();
            } catch (AmazonServiceException e) {
                LOGGER.info(e.getErrorMessage());
                System.exit(1);
            } catch (FileNotFoundException e) {
                LOGGER.info(e.getMessage());
                System.exit(1);
            } catch (IOException e) {
                LOGGER.info(e.getMessage());
                System.exit(1);
            }
            LOGGER.info("Download completed");
            return new File(path);
        }
}
