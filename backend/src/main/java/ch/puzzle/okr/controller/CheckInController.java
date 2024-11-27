package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.checkin.CheckInDto;
import ch.puzzle.okr.mapper.checkin.CheckInMapper;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.service.authorization.CheckInAuthorizationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/v2/checkIns")
public class CheckInController {

    private final CheckInMapper checkInMapper;
    private final CheckInAuthorizationService checkInAuthorizationService;

    public CheckInController(CheckInMapper checkInMapper, CheckInAuthorizationService checkInAuthorizationService) {
        this.checkInMapper = checkInMapper;
        this.checkInAuthorizationService = checkInAuthorizationService;
    }

    @Operation(summary = "Get Check-in", description = "Get Check-in by ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Returned a Check-in with a specified ID", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CheckInDto.class))}), @ApiResponse(responseCode = "401", description = "Not authorized to read Check-in", content = @Content), @ApiResponse(responseCode = "404", description = "Did not find a Check-in with a specified ID", content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<CheckInDto> getCheckInById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(checkInMapper.toDto(this.checkInAuthorizationService.getEntityById(id)));
    }

    @Operation(summary = "Update Check-in", description = "Update a Check-in by ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Updated Check-in in db", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CheckInDto.class))}), @ApiResponse(responseCode = "400", description = "Can't create new Check-in, attributes are not set", content = @Content), @ApiResponse(responseCode = "401", description = "Not authorized to update Check-in", content = @Content), @ApiResponse(responseCode = "404", description = "Given ID of Check-in wasn't found.", content = @Content), @ApiResponse(responseCode = "422", description = "Can't update Check-in since Check-in was updated or deleted by another user.", content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<CheckInDto> updateCheckIn(@Parameter(description = "The ID for updating a Check-in.", required = true) @PathVariable Long id, @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Check-in as json to update an existing Check-in.", required = true) @RequestBody CheckInDto checkInDto) {
        CheckIn checkIn = checkInMapper.toCheckIn(checkInDto);
        CheckInDto updatedCheckIn = this.checkInMapper.toDto(this.checkInAuthorizationService.updateEntity(id,
                                                                                                           checkIn));
        return ResponseEntity.status(HttpStatus.OK)
                             .body(updatedCheckIn);
    }

    @Operation(summary = "Create Check-in", description = "Create a new Check-in")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created new Check-in.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CheckInDto.class))}), @ApiResponse(responseCode = "400", description = "Can't create new Check-in, not allowed to give an ID", content = @Content), @ApiResponse(responseCode = "401", description = "Not authorized to create Check-in", content = @Content)})
    @PostMapping
    public ResponseEntity<CheckInDto> createCheckIn(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Check-in as json to create a new Check-in.", required = true) @RequestBody CheckInDto checkInDto) {
        CheckIn checkIn = checkInMapper.toCheckIn(checkInDto);
        CheckInDto createdCheckIn = checkInMapper.toDto(checkInAuthorizationService.createEntity(checkIn));
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(createdCheckIn);
    }

    @Operation(summary = "Delete Check-in by ID", description = "Delete Check-in by ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Deleted Check-in by ID"), @ApiResponse(responseCode = "401", description = "Not authorized to delete Check-in", content = @Content), @ApiResponse(responseCode = "404", description = "Did not find the Check-in with requested ID")})
    @DeleteMapping("/{id}")
    public void deleteCheckInById(@Parameter(description = "The ID of an Check-in to delete it.", required = true) @PathVariable long id) {
        this.checkInAuthorizationService.deleteEntityById(id);
    }
}
