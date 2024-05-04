package nexign_project_maven.brt.repository;

import nexign_project_maven.brt.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberRepository extends JpaRepository<Subscriber, Integer> {
    Subscriber findByPhoneNumber(String phoneNumber);
}
