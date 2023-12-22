package ch.puzzle.okr;

import ch.puzzle.okr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OkrChampionListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final String DELIMITER = ",";

    @Value("${okr.user.champion.emails}")
    private String okrChampionEmails;

    private final UserRepository userRepository;

    public OkrChampionListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String[] championMails = okrChampionEmails.split(DELIMITER);
        for (var mail : championMails) {
            var user = userRepository.findByEmail(mail)
                    .orElseThrow(() -> new RuntimeException("User not found by champion e-mail: " + mail));
            user.setOkrChampion(true);
            userRepository.save(user);
        }
    }
}