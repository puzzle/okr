package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.service.business.OrganisationBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v2/organisations")
public class OrganisationController {
    private final OrganisationBusinessService organisationBusinessService;

    public OrganisationController(OrganisationBusinessService organisationBusinessService) {
        this.organisationBusinessService = organisationBusinessService;
    }

    @Operation(summary = "Get all Organisations", description = "Get all Organisations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all Organisations", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectiveDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to read all Organisations", content = @Content) })
    @GetMapping
    public ResponseEntity<List<Organisation>> getOrganisations() {
        return ResponseEntity.status(HttpStatus.OK).body(organisationBusinessService.getOrganisations());
    }
}
