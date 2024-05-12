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

/**
 * Component responsible for initializing and sending static data to Kafka topics at startup.
 * This includes tariffs and subscriber data, which are essential for the system's operation.
 */
@Component
public class InitDataSender {

    /**
     * Automatically called post-construction to send initial dataset.
     * @throws JsonProcessingException if serialization of data models fails.
     */
    @PostConstruct
    private void init() throws JsonProcessingException {
        sendInitialData();
    }

    /**
     * Sends initial tariff and subscriber data to respective Kafka topics.
     * @throws JsonProcessingException if serialization fails.
     */
    private void sendInitialData() throws JsonProcessingException {
        sendTariffData();
        sendSubscriberData();
    }

    /**
     * Serializes and sends tariff data to a specified Kafka topic.
     * @throws JsonProcessingException if serialization of tariff data fails.
     */
    public static void sendTariffData() throws JsonProcessingException {
        List<Tariff> tariffs = tariffRepository.findAll();
        kafkaTemplate.send(TARIFF_DATA_TOPIC, new ObjectMapper().writeValueAsString(tariffs));
    }

    /**
     * Serializes and sends subscriber data to a specified Kafka topic.
     * @throws JsonProcessingException if serialization of subscriber data fails.
     */
    public static void sendSubscriberData() throws JsonProcessingException {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        kafkaTemplate.send(SUBSCRIBER_DATA_TOPIC, new ObjectMapper().writeValueAsString(subscribers));
    }
}
