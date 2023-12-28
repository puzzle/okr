package ch.puzzle.okr.applicationlistener;

import ch.puzzle.okr.service.persistence.UserPersistenceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OkrChampionApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final String DELIMITER = ",";

    @Value("${okr.user.champion.emails}")
    private String okrChampionEmails;

    private final UserPersistenceService userPersistenceService;

    public OkrChampionApplicationListener(UserPersistenceService userPersistenceService) {
        this.userPersistenceService = userPersistenceService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String[] championMails = okrChampionEmails.split(DELIMITER);
        for (var mail : championMails) {
            var user = userPersistenceService.findByEmail(mail.trim())
                    .orElseThrow(() -> new RuntimeException("User not found by champion e-mail: " + mail));
            user.setOkrChampion(true);
            userPersistenceService.save(user);
        }
    }
}