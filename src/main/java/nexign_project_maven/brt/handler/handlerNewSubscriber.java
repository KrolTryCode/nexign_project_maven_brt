package nexign_project_maven.brt.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import nexign_project_maven.brt.request.CreateSubscriberRequest;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static nexign_project_maven.brt.handler.InitDataSender.sendSubscriberData;

@Aspect
@Component
public class handlerNewSubscriber {

    @Pointcut("execution(* nexign_project_maven.brt.controller.ManagerController.save(..)) && args(createSubscriberRequest)")
    private void saveMethod(CreateSubscriberRequest createSubscriberRequest) {
    }

    @AfterReturning(pointcut = "saveMethod(createSubscriberRequest)", returning = "response", argNames = "response,createSubscriberRequest")
    public void afterSavingSubscriber(ResponseEntity<String> response, CreateSubscriberRequest createSubscriberRequest) throws JsonProcessingException {
        if (response.getStatusCode() == HttpStatus.OK) {
            sendSubscriberData();
        }
    }
}