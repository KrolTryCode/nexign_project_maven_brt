package nexign_project_maven.brt.brt_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nexign_project_maven.brt.model.Subscriber;
import nexign_project_maven.brt.model.Tariff;
import nexign_project_maven.brt.repository.SubscriberRepository;
import nexign_project_maven.brt.repository.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import static nexign_project_maven.brt.utils.Constants.*;


@Service
public class BrtService {

    private static SubscriberRepository subscriberRepository;
    private static TariffRepository tariffRepository;
    private static KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    public BrtService(SubscriberRepository subscriberRepository, TariffRepository tariffRepository,  KafkaTemplate<String, String> kafkaTemplate) {
        BrtService.subscriberRepository = subscriberRepository;
        BrtService.kafkaTemplate = kafkaTemplate;
        BrtService.tariffRepository = tariffRepository;
    }

    @PostConstruct
    public void init() throws JsonProcessingException {
        sendInitialData();
    }

    private void sendInitialData() throws JsonProcessingException {
        List<Tariff> tariffs = tariffRepository.findAll();
        kafkaTemplate.send(TARIFF_DATA_TOPIC, new ObjectMapper().writeValueAsString(tariffs));

        List<Subscriber> subscribers = subscriberRepository.findAll();
        kafkaTemplate.send(SUBSCRIBER_DATA_TOPIC, new ObjectMapper().writeValueAsString(subscribers));
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
    public void debitingFunds(String paymentRecord) {
        String[] data = paymentRecord.split(",");

        String servedPhoneNumber = data[0];
        double payment = Double.parseDouble(data[1]);

        double balance = subscriberRepository.findByPhoneNumber(servedPhoneNumber).getBalance().doubleValue();

        subscriberRepository.findByPhoneNumber(servedPhoneNumber).setBalance(BigDecimal.valueOf(balance - payment));
    }
}
