package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.models.OrganisationState;
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

    public void saveIfNotExists(Organisation org) {
        if (!getOrgRepository().existsOrganisationByOrgName(org.getOrgName())) {
            repository.save(org);
        }
    }

    private OrganisationRepository getOrgRepository() {
        return (OrganisationRepository) repository;
    }

    public void updateOrganisationStateToInactive(String orgName) {
        Organisation orgInactive = getOrgRepository().findByOrgName(orgName);
        orgInactive.setState(OrganisationState.INACTIVE);
        repository.save(orgInactive);
    }
}
