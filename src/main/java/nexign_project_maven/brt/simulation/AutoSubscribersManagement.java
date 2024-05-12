package nexign_project_maven.brt.simulation;

import nexign_project_maven.brt.request.ChangeTariffRequest;
import nexign_project_maven.brt.request.PaymentRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Random;

import static nexign_project_maven.brt.brt_service.BrtService.subscriberRepository;
import static nexign_project_maven.brt.brt_service.BrtService.tariffRepository;
import static nexign_project_maven.brt.controller.ManagerController.changeTariff;
import static nexign_project_maven.brt.controller.SubscriberController.payBalance;
import static nexign_project_maven.brt.utils.Constants.*;

@Component
public class AutoSubscribersManagement {

    Random random = new Random();

    @KafkaListener(topics = MONTH_EVENT_TOPIC, groupId = GROUP_ID)
    public void simulationSubscriber() {
        paySimulation();
        changeTariffSimulation();
    }


    private void paySimulation() {
        subscriberRepository.findAll().forEach(subscriber -> {
            Principal principal = subscriber::getPhoneNumber;
            payBalance(new PaymentRequest(BigDecimal.valueOf(random.nextInt(RANGE_PAY + 1))), principal);
        });
    }

    private void changeTariffSimulation() {
        int k = 0;
        int numberTariffChanges = random.nextInt(NUMBER_CHANGE_TARIFF_DOWN,NUMBER_CHANGE_TARIFF_UPPER + 1);
        while (k < numberTariffChanges) {
            changeTariff(new ChangeTariffRequest(subscriberRepository.findRandomSubscriber().getPhoneNumber(), tariffRepository.findRandomTariff().getId()));
            k++;
        }
    }
}
