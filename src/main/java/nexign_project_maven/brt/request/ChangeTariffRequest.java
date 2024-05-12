package nexign_project_maven.brt.request;

import io.swagger.v3.oas.annotations.media.Schema;

public class ChangeTariffRequest {
    @Schema(description = "Номер телефона абонента", example = "")
    private String msisdn;

    @Schema(description = "ID тарифа абонента", example = "11")
    private Long tariffId; // Сумма пополнения

    public ChangeTariffRequest(String msisdn, Long tariffId) {
        this.msisdn = msisdn;
        this.tariffId = tariffId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public Long getTariffId() {
        return tariffId;
    }
}
