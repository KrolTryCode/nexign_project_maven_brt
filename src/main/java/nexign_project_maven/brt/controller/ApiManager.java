package nexign_project_maven.brt.controller;

import nexign_project_maven.brt.exeption.ApiManagerException;
import nexign_project_maven.brt.model.Subscriber;
import nexign_project_maven.brt.model.Tariff;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static nexign_project_maven.brt.brt_service.BrtService.subscriberRepository;
import static nexign_project_maven.brt.brt_service.BrtService.tariffRepository;

@Component
public class ApiManager {

    public static boolean isSubscriberExists(String msisdn) {
        return subscriberRepository.findByPhoneNumber(msisdn) != null;
    }

    public static boolean isTariffExists(Long tariffId) {
        return tariffRepository.findTariffById(tariffId) != null;
    }


    @Transactional
    public void pay(String msisdn, BigDecimal amount) {
        Subscriber subscriber = subscriberRepository.findByPhoneNumber(msisdn);
        subscriber.setBalance(subscriber.getBalance().add(amount));
        subscriberRepository.save(subscriber);
    }


    @Transactional
    public void changeTariff(String msisdn, Long tariffId) {
        if (!isSubscriberExists(msisdn)) {
            throw new ApiManagerException("Абонента с MSISDN " + msisdn + " не существует.");
        }

        if (!isTariffExists(tariffId)) {
            throw new ApiManagerException("Тарифа с ID " + tariffId + " не существует.");
        }

        Subscriber subscriber = subscriberRepository.findByPhoneNumber(msisdn);

        Tariff tariff = tariffRepository.findTariffById(tariffId);

        subscriber.setTariff(tariff);
        subscriberRepository.save(subscriber);
    }


    @Transactional
    public void save(String msisdn, BigDecimal money, Long tariffId) {
        if (isSubscriberExists(msisdn)) {
            throw new ApiManagerException("Абонент с MSISDN " + msisdn + " уже существует.");
        }

        if (!isTariffExists(tariffId)) {
            throw new ApiManagerException("Тарифа с ID " + tariffId + " не существует.");
        }

        Subscriber subscriber = new Subscriber();
        subscriber.setPhoneNumber(msisdn);
        subscriber.setBalance(money);

        Tariff tariff = tariffRepository.findTariffById(tariffId);

        subscriber.setTariff(tariff);
        subscriberRepository.save(subscriber);
    }
}
