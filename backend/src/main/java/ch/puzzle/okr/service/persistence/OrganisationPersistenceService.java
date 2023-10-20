package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.repository.OrganisationRepository;
import org.springframework.stereotype.Service;

@Service
public class OrganisationPersistenceService extends PersistenceBase<Organisation, Long> {

    protected OrganisationPersistenceService(OrganisationRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return "organisation";
    }

    public Organisation saveIfNotExists(Organisation org) {
        return getOrgRepository().findByOrgName(org.getOrgName()).orElseGet(() -> repository.save(org));
    }

    private OrganisationRepository getOrgRepository() {
        return (OrganisationRepository) repository;
    }
}
