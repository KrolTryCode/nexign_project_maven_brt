package nexign_project_maven.brt.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import nexign_project_maven.brt.request.ChangeTariffRequest;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static nexign_project_maven.brt.handler.InitDataSender.sendSubscriberData;

public class handlerChangeTariff {

    @Pointcut("execution(* nexign_project_maven.brt.controller.ManagerController.changeTariff(..)) && args(changeTariffRequest)")
    private void changeMethod(ChangeTariffRequest changeTariffRequest) {
    }

    @AfterReturning(pointcut = "changeMethod(changeTariffRequest)", returning = "response", argNames = "response,changeTariffRequest")
    public synchronized void afterChangeTariff(ResponseEntity<String> response, ChangeTariffRequest changeTariffRequest) throws JsonProcessingException {
        if (response.getStatusCode() == HttpStatus.OK) {
            sendSubscriberData();
        }
    }
}
