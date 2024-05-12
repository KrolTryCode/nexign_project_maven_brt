package nexign_project_maven.brt.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nexign_project_maven.brt.model.Subscriber;
import nexign_project_maven.brt.model.Tariff;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

import static nexign_project_maven.brt.brt_service.BrtService.*;
import static nexign_project_maven.brt.brt_service.BrtService.kafkaTemplate;
import static nexign_project_maven.brt.utils.Constants.SUBSCRIBER_DATA_TOPIC;
import static nexign_project_maven.brt.utils.Constants.TARIFF_DATA_TOPIC;

@Component
public class InitDataSender {

    @PostConstruct
    private void init() throws JsonProcessingException {
        sendInitialData();
    }

    private void sendInitialData() throws JsonProcessingException {
        sendTariffData();
        sendSubscriberData();
    }

    public static void sendTariffData() throws JsonProcessingException {
        List<Tariff> tariffs = tariffRepository.findAll();
        kafkaTemplate.send(TARIFF_DATA_TOPIC, new ObjectMapper().writeValueAsString(tariffs));
    }

    public static void sendSubscriberData() throws JsonProcessingException {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        kafkaTemplate.send(SUBSCRIBER_DATA_TOPIC, new ObjectMapper().writeValueAsString(subscribers));
    }
}
