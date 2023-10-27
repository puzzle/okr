package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Organisation;
import org.springframework.data.repository.CrudRepository;

public interface OrganisationRepository extends CrudRepository<Organisation, Long> {

    Organisation findByOrgName(String name);

    boolean existsOrganisationByOrgName(String name);
}
