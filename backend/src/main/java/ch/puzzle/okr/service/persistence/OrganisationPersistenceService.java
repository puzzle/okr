package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.models.OrganisationState;
import ch.puzzle.okr.repository.OrganisationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static ch.puzzle.okr.Constants.ORGANISATION;

@Service
public class OrganisationPersistenceService extends PersistenceBase<Organisation, Long, OrganisationRepository> {

    protected OrganisationPersistenceService(OrganisationRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return ORGANISATION;
    }

    public Organisation saveIfNotExists(Organisation org) {
        if (!getRepository().existsOrganisationByOrgName(org.getOrgName())) {
            return getRepository().save(org);
        }
        return null;
    }

    public void updateOrganisationStateToInactive(String orgName) {
        Organisation orgInactive = getRepository().findByOrgName(orgName);
        orgInactive.setState(OrganisationState.INACTIVE);
        getRepository().save(orgInactive);
    }

    public List<Organisation> getOrganisationsByTeamId(Long teamId) {
        return getRepository().findOrganisationsByTeamId(teamId);
    }

    public List<Organisation> getActiveOrganisations() {
        return getRepository().findOrganisationsByState(OrganisationState.ACTIVE);
    }
}
