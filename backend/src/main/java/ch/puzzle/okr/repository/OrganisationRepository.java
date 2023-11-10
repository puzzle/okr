package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Organisation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrganisationRepository extends CrudRepository<Organisation, Long> {

    Organisation findByOrgName(String name);

    boolean existsOrganisationByOrgName(String name);

    @Query(value = "SELECT * FROM organisation LEFT JOIN team_organisation ON organisation.id=team_organisation.organisation_id WHERE team_organisation.team_id = :id", nativeQuery = true)
    List<Organisation> findOrganisationsByTeamId(@Param("id") Long id);
}
