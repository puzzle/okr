package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.UserTeam;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.CacheService;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import ch.puzzle.okr.service.persistence.UserTeamPersistenceService;
import ch.puzzle.okr.service.validation.TeamValidationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class TeamBusinessService {

    private final TeamPersistenceService teamPersistenceService;

    private final ObjectiveBusinessService objectiveBusinessService;

    private final UserPersistenceService userPersistenceService;

    private final UserTeamPersistenceService userTeamPersistenceService;

    private final TeamValidationService validator;
    private final CacheService cacheService;

    public TeamBusinessService(TeamPersistenceService teamPersistenceService,
            ObjectiveBusinessService objectiveBusinessService, TeamValidationService validator,
            CacheService cacheService, UserPersistenceService userPersistenceService,
                               UserTeamPersistenceService userTeamPersistenceService) {
        this.teamPersistenceService = teamPersistenceService;
        this.objectiveBusinessService = objectiveBusinessService;
        this.userPersistenceService = userPersistenceService;
        this.validator = validator;
        this.cacheService = cacheService;
        this.userTeamPersistenceService = userTeamPersistenceService;
    }

    public Team getTeamById(Long teamId) {
        validator.validateOnGet(teamId);
        return teamPersistenceService.findById(teamId);
    }

    @Transactional
     // Creates a new team. Current authorization user is added as admin user in team.
    public Team createTeam(Team team, AuthorizationUser authorizationUser) {
        validator.validateOnCreate(team);
        cacheService.emptyAuthorizationUsersCache();
        var savedTeam = teamPersistenceService.save(team);
        var currentUser = authorizationUser.user();
        addTeamMembership(savedTeam.getId(), true, currentUser, currentUser.getUserTeamList());
        userPersistenceService.save(currentUser);
        return savedTeam;
    }

    public Team updateTeam(Team team, Long id) {
        validator.validateOnUpdate(id, team);
        cacheService.emptyAuthorizationUsersCache();
        return teamPersistenceService.save(team);
    }

    @Transactional
    public void deleteTeam(Long id) {
        validator.validateOnDelete(id);
        objectiveBusinessService.getEntitiesByTeamId(id)
                .forEach(objective -> objectiveBusinessService.deleteEntityById(objective.getId()));
        deleteUserTeamList(id);
        cacheService.emptyAuthorizationUsersCache();
        teamPersistenceService.deleteById(id);
    }

    private void deleteUserTeamList(Long id) {
        var team = teamPersistenceService.findById(id);
        // remove userTeam from each user, otherwise they are still in the session and are not deleted
        team.getUserTeamList().forEach(userTeam -> {
            var user = userTeam.getUser();
            user.getUserTeamList().remove(userTeam);
        });
        userTeamPersistenceService.deleteAll(team.getUserTeamList());
        team.setUserTeamList(List.of());
    }

    public List<Team> getAllTeams(AuthorizationUser authorizationUser) {
        List<Team> teams = teamPersistenceService.findAll();
        List<Team> mutableTeams = new ArrayList<>(teams);
        return sortTeams(mutableTeams, authorizationUser);
    }

    private List<Team> sortTeams(List<Team> teams, AuthorizationUser authorizationUser) {
        teams.sort(new TeamComparator(authorizationUser));
        return teams;
    }

    @Transactional
    public void addUsersToTeam(long teamId, List<Long> userIdList) {
        var team = teamPersistenceService.findById(teamId);
        for (var userId : userIdList) {
            var user = userPersistenceService.findById(userId);
            var userTeam = UserTeam.Builder.builder().withTeam(team).withUser(user).withTeamAdmin(false).build();
            user.getUserTeamList().add(userTeam);
            userPersistenceService.save(user);
        }
    }

    @Transactional
    public void removeUserFromTeam(long teamId, long userId) {
        var user = userPersistenceService.findById(userId);
        var userTeamList = user.getUserTeamList();
        var userTeamToRemove = userTeamList.stream()
                .filter(ut -> ut.getTeam().getId() == teamId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No team removed from userTeam list"));
        userTeamList.remove(userTeamToRemove);
        userTeamPersistenceService.delete(userTeamToRemove);
        userPersistenceService.save(user);
    }

    public void updateOrAddTeamMembership(long teamId, long userId, boolean isAdmin) {
        var user = userPersistenceService.findById(userId);
        List<UserTeam> userTeamList = user.getUserTeamList();
        for (var ut : userTeamList) {
            if (ut.getTeam().getId().equals(teamId)) {
                ut.setTeamAdmin(isAdmin);
                userPersistenceService.save(user);
                return;
            }
        }
        // if user has no membership to this team, it is added.
        addTeamMembership(teamId, isAdmin, user, userTeamList);
        userPersistenceService.save(user);
    }

    private void addTeamMembership(long teamId, boolean isAdmin, User user, List<UserTeam> userTeamList) {
        var team = teamPersistenceService.findById(teamId);
        var userTeam = UserTeam.Builder.builder()
                .withTeam(team)
                .withTeamAdmin(isAdmin)
                .withUser(user)
                .build();
        userTeamList.add(userTeam);
    }

    private record TeamComparator(AuthorizationUser authorizationUser) implements Comparator<Team> {
        @Override
        public int compare(Team t1, Team t2) {
            boolean isUserTeam1 = authorizationUser.isUserMemberInTeam(t1.getId());
            boolean isUserTeam2 = authorizationUser.isUserMemberInTeam(t2.getId());

            if (isUserTeam1 == isUserTeam2) {
                if (Objects.equals(t1.getName(), t2.getName())) {
                    return t1.getId().compareTo(t2.getId());
                } else {
                    return t1.getName().compareTo(t2.getName());
                }
            }
            return isUserTeam1 ? -1 : 1;
        }
    }
}
