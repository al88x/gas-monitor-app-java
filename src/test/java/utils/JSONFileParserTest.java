package utils;

import model.GasReading;
import model.Location;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


public class JSONFileParserTest {


    @Test
    public void parseValidJsonFile() throws FileNotFoundException {
        //Arrange
        File validJsonFile = new File("src/test/java/files/validLocations.json");

        //Act
        List<Location> locations = JSONFileParser.parseLocationFile(validJsonFile);

        //Assert
        assertThat(locations.size()).isEqualTo(4);
        assertThat(locations.get(0).getId()).isEqualTo(new String("79e14e8f-e531-46f0-90a0-2d43ae5366fc"));
        assertThat(locations.get(0).getX()).isEqualTo(979.4060233428987);
        assertThat(locations.get(0).getY()).isEqualTo(351.5988679928176);
    }

    @Test
    public void parseSqsMessageWithValidReadingMessage() {
        //Arrange
        String sqsMessageBodyWithValidReadingMessage = "{\n" +
                "  \"Type\" : \"Notification\",\n" +
                "  \"MessageId\" : \"89ee851a-cd59-52ff-931b-d376e8d99d19\",\n" +
                "  \"TopicArn\" : \"arn:aws:sns:eu-west-2:099421490492:EventProcessing-snsTopicSensorDataPart1-QVDE0JIXZS1V\",\n" +
                "  \"Message\" : \"{\\\"locationId\\\":\\\"4161a711-efe6-4bee-a486-d97d0f94a02c\\\",\\\"eventId\\\":\\\"654b1799-6de6-467d-b680-730d762aad3f\\\",\\\"value\\\":6.8561332379019095,\\\"timestamp\\\":1576149824757}\",\n" +
                "  \"Timestamp\" : \"2019-12-12T11:23:44.762Z\",\n" +
                "  \"SignatureVersion\" : \"1\",\n" +
                "  \"Signature\" : \"dt7tAOuSBmEXG8JwTpSkIXFs/6RoWXTsSAHlhib9IqWI51xqnxvfrLzoCLVlYH5mFnkDaz0obmh8RbvzJK8Kuuh30N9IMo2srDeqZC+IjD1OzKiwUF4yy+CrAbpH6FiklxZ9cR5IwWeE4XJkD+1eFzCwj0jmOVNxkLa2Pf2XkhfMV2hy71E/H9tcTnKJcm1yHyDnQ4I5mBt0eGgmEv+EFZi9UKdyIDlgeq67dcs2MedqV0Nl+YIEVTmXmpRceMd+FMIWP6eZ5TUiq+/MG8doG0o+xl0rt6NGAmX74c9ZW/0903DqAlZuRhflimjCdlPYXl2INTCZt5PpmHSqo0WV+g==\",\n" +
                "  \"SigningCertURL\" : \"https://sns.eu-west-2.amazonaws.com/SimpleNotificationService-6aad65c2f9911b05cd53efda11f913f9.pem\",\n" +
                "  \"UnsubscribeURL\" : \"https://sns.eu-west-2.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:eu-west-2:099421490492:EventProcessing-snsTopicSensorDataPart1-QVDE0JIXZS1V:c06fe97a-e9cd-4ff3-ad98-18fe8bfc7dee\"\n" +
                "}";

        //Act
        GasReading gasReading = JSONFileParser.parseSqsMessage(sqsMessageBodyWithValidReadingMessage);

        //Assert
        assertThat(gasReading.getLocationId()).isEqualTo(new String("4161a711-efe6-4bee-a486-d97d0f94a02c"));
        assertThat(gasReading.getEventId()).isEqualTo(new String("654b1799-6de6-467d-b680-730d762aad3f"));
        assertThat(gasReading.getValue()).isEqualTo(6.8561332379019095);
        assertThat(gasReading.getTimestamp()).isEqualTo(Long.valueOf("1576149824757"));
    }

    @Test
    public void parseSqsMessageWithInvalidReadingMessage(){
        //Arrange
        String sqsMessageBodyWithInvalidReadingMessage = "{\n" +
                "  \"Type\" : \"Notification\",\n" +
                "  \"MessageId\" : \"89ee851a-cd59-52ff-931b-d376e8d99d19\",\n" +
                "  \"TopicArn\" : \"arn:aws:sns:eu-west-2:099421490492:EventProcessing-snsTopicSensorDataPart1-QVDE0JIXZS1V\",\n" +
                "  \"Message\" : \"{\\\"eventId\\\":\\\"654b1799-6de6-467d-b680-730d762aad3f\\\",\\\"value\\\":6.8561332379019095,\\\"timestamp\\\":1576149824757}\",\n" +
                "  \"Timestamp\" : \"2019-12-12T11:23:44.762Z\",\n" +
                "  \"SignatureVersion\" : \"1\",\n" +
                "  \"Signature\" : \"dt7tAOuSBmEXG8JwTpSkIXFs/6RoWXTsSAHlhib9IqWI51xqnxvfrLzoCLVlYH5mFnkDaz0obmh8RbvzJK8Kuuh30N9IMo2srDeqZC+IjD1OzKiwUF4yy+CrAbpH6FiklxZ9cR5IwWeE4XJkD+1eFzCwj0jmOVNxkLa2Pf2XkhfMV2hy71E/H9tcTnKJcm1yHyDnQ4I5mBt0eGgmEv+EFZi9UKdyIDlgeq67dcs2MedqV0Nl+YIEVTmXmpRceMd+FMIWP6eZ5TUiq+/MG8doG0o+xl0rt6NGAmX74c9ZW/0903DqAlZuRhflimjCdlPYXl2INTCZt5PpmHSqo0WV+g==\",\n" +
                "  \"SigningCertURL\" : \"https://sns.eu-west-2.amazonaws.com/SimpleNotificationService-6aad65c2f9911b05cd53efda11f913f9.pem\",\n" +
                "  \"UnsubscribeURL\" : \"https://sns.eu-west-2.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:eu-west-2:099421490492:EventProcessing-snsTopicSensorDataPart1-QVDE0JIXZS1V:c06fe97a-e9cd-4ff3-ad98-18fe8bfc7dee\"\n" +
                "}";

        //Act
        GasReading gasReading = JSONFileParser.parseSqsMessage(sqsMessageBodyWithInvalidReadingMessage);

        //Assert
        assertThat(gasReading).isEqualTo(null);
    }
}
