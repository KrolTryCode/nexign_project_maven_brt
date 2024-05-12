package nexign_project_maven.brt.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

import static nexign_project_maven.brt.utils.Constants.DEFAULT_MONEY;

public class CreateSubscriberRequest {
    @Schema(description = "Номер телефона абонента", example = "")
    private String msisdn;

    @Schema(description = "Баланс абонента (default = 100)", example = "100")
    private BigDecimal money = DEFAULT_MONEY; // Сумма пополнения

    @Schema(description = "ID тарифа абонента", example = "11")
    private Long tariffId;

    public CreateSubscriberRequest(String msisdn, BigDecimal money, Long tariffId) {
        this.msisdn = msisdn;
        this.money = money;
        this.tariffId = tariffId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public Long getTariffId() {
        return tariffId;
    }
}