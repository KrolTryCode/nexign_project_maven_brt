package nexign_project_maven.brt.brt_service;

import nexign_project_maven.brt.model.Subscriber;
import nexign_project_maven.brt.repository.SubscriberRepository;
import nexign_project_maven.brt.repository.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static nexign_project_maven.brt.utils.Constants.*;
/**
 * Service class that handles business logic related to billing and telecommunications rate management.
 * It listens to Kafka topics for CDR (Call Detail Records) and payment data to process and manage subscriber billing.
 */
@Service
public class BrtService {

    public static SubscriberRepository subscriberRepository;
    public  static TariffRepository tariffRepository;
    public static KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Constructs a BrtService with necessary repository and Kafka template.
     *
     * @param subscriberRepository Repository for subscriber data access
     * @param tariffRepository Repository for tariff data access
     * @param kafkaTemplate Kafka template for messaging
     */
    @Autowired
    public BrtService(SubscriberRepository subscriberRepository, TariffRepository tariffRepository,  KafkaTemplate<String, String> kafkaTemplate) {
        BrtService.subscriberRepository = subscriberRepository;
        BrtService.kafkaTemplate = kafkaTemplate;
        BrtService.tariffRepository = tariffRepository;
    }


    /**
     * Listens to the Kafka topic for CDR and enriches each record with tariff information before sending to another topic.
     *
     * @param fileContent The raw content of CDR files received as a string
     */
    @KafkaListener(topics = CDR_TOPIC, groupId = GROUP_ID)
    public static void receiveFileContent(String fileContent) {
        String[] lines = fileContent.split(System.lineSeparator());
        for (String line : lines) {
            String[] data = line.split(",");
            if (data.length > 1) {
                Subscriber subscriber = subscriberRepository.findByPhoneNumber(data[1]);
                if (subscriber != null) {
                    String result = line + "," + subscriber.getTariffId();
                    kafkaTemplate.send(AUTH_RECORDS_TOPIC, result);
                }
            }
        }
    }

    /**
     * Processes incoming payment records from Kafka, debits subscriber accounts, and updates their balance.
     * This method is synchronized and transactional to ensure data integrity.
     *
     * @param paymentRecord The payment record received from Kafka
     */
    @Transactional
    @KafkaListener(topics = PAYMENT_TOPIC, groupId = GROUP_ID)
    public synchronized void debitingFunds(String paymentRecord) {
        System.out.println(paymentRecord);
        String[] data = paymentRecord.split(",");

        String servedPhoneNumber = data[0];
        double payment = Double.parseDouble(data[1]);

        double balance = subscriberRepository.findByPhoneNumber(servedPhoneNumber).getBalance().doubleValue();

        subscriberRepository.findByPhoneNumber(servedPhoneNumber).setBalance(BigDecimal.valueOf(balance - payment));
    }
}
