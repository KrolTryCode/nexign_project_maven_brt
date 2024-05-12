package nexign_project_maven.brt.controller;

import nexign_project_maven.brt.exeption.ApiManagerException;
import nexign_project_maven.brt.model.Subscriber;
import nexign_project_maven.brt.model.Tariff;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static nexign_project_maven.brt.brt_service.BrtService.subscriberRepository;
import static nexign_project_maven.brt.brt_service.BrtService.tariffRepository;

/**
 * API Manager for handling business logic related to subscribers and tariffs.
 * Provides methods to pay, change tariff, and create new subscriber entries.
 */
@Component
public class ApiManager {

    /**
     * Checks if a subscriber exists in the database based on their MSISDN.
     *
     * @param msisdn The mobile subscriber ISDN number.
     * @return true if the subscriber exists, false otherwise.
     */
    public static boolean isSubscriberExists(String msisdn) {
        return subscriberRepository.findByPhoneNumber(msisdn) != null;
    }

    /**
     * Checks if a tariff plan exists in the database based on the tariff ID.
     *
     * @param tariffId The ID of the tariff plan.
     * @return true if the tariff exists, false otherwise.
     */
    public static boolean isTariffExists(Long tariffId) {
        return tariffRepository.findTariffById(tariffId) != null;
    }

    /**
     * Processes payment for a subscriber and updates their balance.
     *
     * @param msisdn The mobile subscriber ISDN number.
     * @param amount The amount to be added to the subscriber's balance.
     * @throws ApiManagerException if the subscriber does not exist.
     */
    @Transactional
    public void pay(String msisdn, BigDecimal amount) {
        Subscriber subscriber = subscriberRepository.findByPhoneNumber(msisdn);
        subscriber.setBalance(subscriber.getBalance().add(amount));
        subscriberRepository.save(subscriber);
    }

    /**
     * Changes the tariff plan of a subscriber.
     *
     * @param msisdn The mobile subscriber ISDN number.
     * @param tariffId The new tariff plan ID to be assigned to the subscriber.
     * @throws ApiManagerException if the subscriber or tariff does not exist.
     */
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

    /**
     * Creates a new subscriber entry in the database.
     *
     * @param msisdn The mobile subscriber ISDN number.
     * @param money Initial balance to be set for the new subscriber.
     * @param tariffId Tariff plan ID to be assigned to the new subscriber.
     * @throws ApiManagerException if the subscriber already exists or the tariff does not exist.
     */
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
