package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Organisation;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrganisationRepository extends CrudRepository<Organisation, Long> {
    Optional<Organisation> findByOrgName(String name);
}
