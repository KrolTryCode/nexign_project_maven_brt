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

@Service
public class BrtService {

    public static SubscriberRepository subscriberRepository;
    public  static TariffRepository tariffRepository;
    public static KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public BrtService(SubscriberRepository subscriberRepository, TariffRepository tariffRepository,  KafkaTemplate<String, String> kafkaTemplate) {
        BrtService.subscriberRepository = subscriberRepository;
        BrtService.kafkaTemplate = kafkaTemplate;
        BrtService.tariffRepository = tariffRepository;
    }


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
