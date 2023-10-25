package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends CrudRepository<Team, Long> {

    @Query(value = """
            select distinct teamOrg.team_id from team_organisation teamOrg
              inner join organisation o on o.id = teamOrg.organisation_id
            where o.org_name in (:organisationNames)
            """, nativeQuery = true)
    List<Long> findTeamIdsByOrganisationNames(@Param("organisationNames") List<String> organisationNames);
}
