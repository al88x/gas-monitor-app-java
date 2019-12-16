package utils;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.util.Topics;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import org.apache.logging.log4j.LogManager;

import java.io.Closeable;
import java.util.List;
import java.util.stream.Collectors;

public class AWSQueue implements Closeable {

    private AmazonSQS sqs;
    private AmazonSNS sns;
    private String queueUrl;
    private ReceiveMessageRequest receiveMessageRequest;
    private String topicSubscribeUrl;
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();

    public AWSQueue(String queueName, String topicArn) {
        LOGGER.info("Connecting to AWS...");
        sqs = AmazonSQSClientBuilder.defaultClient();
        sns = AmazonSNSClientBuilder.defaultClient();
        queueUrl = sqs.createQueue(queueName).getQueueUrl();
        topicSubscribeUrl = Topics.subscribeQueue(sns, sqs, topicArn, queueUrl);
        receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
                .withMaxNumberOfMessages(10)
                .withWaitTimeSeconds(5);
        LOGGER.info("Successfully connected to AWS and created queue");
    }

    public List<Message> getMessageFromSQSAndDeleteFromQueue() {
        return sqs.receiveMessage(receiveMessageRequest).getMessages().stream()
                .peek(message -> sqs.deleteMessage(new DeleteMessageRequest().withQueueUrl(queueUrl).withReceiptHandle(message.getReceiptHandle())))
                .collect(Collectors.toList());
    }

    @Override
    public void close() {
        sqs.deleteQueue(queueUrl);
        LOGGER.info("Deleted queue from AWS");
        sns.unsubscribe(topicSubscribeUrl);
        LOGGER.info("Unsubscribed from AWS Topic");
    }
}
