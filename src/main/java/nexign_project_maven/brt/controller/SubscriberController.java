package nexign_project_maven.brt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nexign_project_maven.brt.request.PaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static nexign_project_maven.brt.controller.ManagerController.apiManager;


@RestController
@RequestMapping("/api/subscriber")
public class SubscriberController {

    @PostMapping("/pay")
    @Operation(summary = "Пополнение баланса", description = "Пополнение баланса абонента на заданное значение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное пополнение",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Неавторизованный вход: требуется аутентификация или она не удалась",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Текущая роль не имеет доступа к данному методу",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    public synchronized static ResponseEntity<String> payBalance(@RequestBody PaymentRequest paymentRequest, Principal principal) {
            String msisdn = principal.getName(); // номер телефона из аутентификации
            apiManager.pay(msisdn, paymentRequest.getMoney());
            System.out.println(msisdn + " " + paymentRequest.getMoney());
            return ResponseEntity.ok("Баланс успешно пополнен");
    }
}