package nexign_project_maven.brt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nexign_project_maven.brt.exeption.ApiManagerException;
import nexign_project_maven.brt.request.ChangeTariffRequest;
import nexign_project_maven.brt.request.CreateSubscriberRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {
    public static ApiManager apiManager;

    @Autowired
    public ManagerController(ApiManager apiManager) {
        ManagerController.apiManager = apiManager;
    }

    @PostMapping("/changeTariff")
    @Operation(summary = "Смена тарифа", description = "Меняет тариф заданному абоненту на указанный")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Тариф успешно изменен",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Неавторизованный вход: требуется аутентификация или она не удалась",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Текущая роль не имеет доступа к данному методу",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Указанный/ые аттрибуты некорректны",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    public synchronized static ResponseEntity<String> changeTariff(@RequestBody ChangeTariffRequest changeTariffRequest) {
        try {
            apiManager.changeTariff(changeTariffRequest.getMsisdn(), changeTariffRequest.getTariffId());
            return ResponseEntity.ok("Тариф успешно изменен");
        } catch (ApiManagerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PostMapping("/save")
    @Operation(summary = "Создание абонента", description = "Создает абонент с заданным номером, балансом(по умолчанию 100) и тарифом")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Абонент успешно создан",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Неавторизованный вход: требуется аутентификация или она не удалась",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Текущая роль не имеет доступа к данному методу",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Абонент с таким номером уже существует или такого тарифа не существует",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<String> save(@RequestBody CreateSubscriberRequest createSubscriberRequest) {
        try {
            apiManager.save(createSubscriberRequest.getMsisdn(), createSubscriberRequest.getMoney() ,createSubscriberRequest.getTariffId());
            return ResponseEntity.ok("Абонент успешно создан");
        } catch (ApiManagerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
