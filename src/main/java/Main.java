import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import service.GasReadingService;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.util.Topics;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import model.GasReading;
import model.Location;
import utils.AWSUtils;
import utils.JSONFileParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;


public class Main {



    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        File locationsJsonFile = AWSUtils.fetchLocationFileFromS3Bucket(args[0], args[1]);
        List<Location> locations = JSONFileParser.parseLocationFile(locationsJsonFile);

        GasReadingService gasReadingService = new GasReadingService(locations);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        AmazonSNS sns = AmazonSNSClientBuilder.defaultClient();

        String myQueueUrl = sqs.createQueue("gas-sensor-data").getQueueUrl();
        String myTopicArn = args[2];
        Topics.subscribeQueue(sns, sqs, myTopicArn, myQueueUrl);

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
        receiveMessageRequest.setMaxNumberOfMessages(10);
        receiveMessageRequest.withWaitTimeSeconds(5);
        long timeNow = System.currentTimeMillis();

        while(System.currentTimeMillis() < timeNow + 15000){
            List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
            if (messages.size() > 0) {
                for(Message message : messages){
                    GasReading gasReading = JSONFileParser.parseSqsMessage(message.getBody());
                    boolean isGasReadingValid = gasReadingService.isValid(gasReading);
                    if (!isGasReadingValid) {
                        continue;
                    }
                    System.out.println("Gas reading: " + gasReading.getValue());
                    String messageReceiptHandle = message.getReceiptHandle();
                    sqs.deleteMessage(new DeleteMessageRequest().withQueueUrl(myQueueUrl).withReceiptHandle(messageReceiptHandle));
                }
            }
        }
        sqs.deleteQueue(myQueueUrl);
    }
}
