package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.OrganisationDto;
import ch.puzzle.okr.mapper.OrganisationMapper;
import ch.puzzle.okr.service.authorization.OrganisationAuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/organisations")
public class OrganisationController {
    private final OrganisationAuthorizationService organisationAuthorizationService;
    private final OrganisationMapper organisationMapper;

    public OrganisationController(OrganisationAuthorizationService organisationAuthorizationService,
            OrganisationMapper organisationMapper) {
        this.organisationAuthorizationService = organisationAuthorizationService;
        this.organisationMapper = organisationMapper;
    }

    @Operation(summary = "Get all active Organisations", description = "Get all active Organisations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all active Organisations", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OrganisationDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to read organisations", content = @Content) })
    @GetMapping
    public ResponseEntity<List<OrganisationDto>> getActiveOrganisations() {
        return ResponseEntity.status(HttpStatus.OK).body(
                organisationAuthorizationService.getEntities().stream().map(this.organisationMapper::toDto).toList());
    }

    @Operation(summary = "Get Organisations of team", description = "Get all Organisations of team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all Organisations of team", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OrganisationDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to read organisations of team", content = @Content) })
    @GetMapping("/{id}")
    public ResponseEntity<List<OrganisationDto>> getOrganisationsByTeam(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(organisationAuthorizationService.getEntitiesByTeam(id).stream()
                .map(this.organisationMapper::toDto).toList());
    }
}
