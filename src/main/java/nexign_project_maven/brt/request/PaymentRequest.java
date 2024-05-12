package nexign_project_maven.brt.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public class PaymentRequest {
    @Schema(description = "Сумма пополнения баланса абонента (default = 100)", example = "100")
    private BigDecimal money;

    public PaymentRequest() {}

    public PaymentRequest(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getMoney() {
        return money;
    }

}