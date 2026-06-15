import { TestBed } from '@angular/core/testing';
import { TeamStateService } from './team.state.service';
import { TeamService } from './team.service';
import { UserService } from './user.service';
import { of } from 'rxjs';
import { Team } from '../shared/types/model/team';
import { User } from '../shared/types/model/user';
import { UserTeam } from '../shared/types/model/user-team';

describe('TeamStateService', () => {
  let service: TeamStateService;
  let teamServiceMock: jest.Mocked<any>;
  let userServiceMock: jest.Mocked<any>;

  const mockTeams: Team[] = [{ id: 1,
    name: 'Alpha Team',
    isWriteable: true,
    markedAsArchivedAt: null } as Team,
  { id: 2,
    name: 'Beta Team',
    isWriteable: false,
    markedAsArchivedAt: null } as Team];

  beforeEach(() => {
    teamServiceMock = {
      getAllTeams: jest.fn()
        .mockReturnValue(of(mockTeams)),
      createTeam: jest.fn()
        .mockReturnValue(of({ id: 3,
          name: 'New Team' } as Team)),
      updateTeam: jest.fn()
        .mockReturnValue(of({ id: 1,
          name: 'Updated Alpha' } as Team)),
      deleteTeam: jest.fn()
        .mockReturnValue(of(null)),
      addUsersToTeam: jest.fn()
        .mockReturnValue(of(null)),
      removeUserFromTeam: jest.fn()
        .mockReturnValue(of(null)),
      updateOrAddTeamMembership: jest.fn()
        .mockReturnValue(of(null)),
      archiveTeam: jest.fn()
        .mockReturnValue(of(null)),
      unarchiveTeam: jest.fn()
        .mockReturnValue(of(null))
    };

    userServiceMock = {
      reloadUsers: jest.fn()
    };

    TestBed.configureTestingModule({
      providers: [TeamStateService,
        { provide: TeamService,
          useValue: teamServiceMock },
        { provide: UserService,
          useValue: userServiceMock }]
    });

    service = TestBed.inject(TeamStateService);
  });

  it('should be created and start with an empty state', (done) => {
    expect(service)
      .toBeTruthy();
    service.getTeams()
      .subscribe((teams) => {
        expect(teams)
          .toEqual([]);
        done();
      });
  });

  describe('State Queries & Filters', () => {
    it('loadTeams should fetch teams via TeamService and update the global stream state', (done) => {
      const filters = { quarterId: 42 };

      service.loadTeams(filters);

      expect(teamServiceMock.getAllTeams)
        .toHaveBeenCalledWith(filters);
      service.getTeams()
        .subscribe((teams) => {
          expect(teams)
            .toEqual(mockTeams);
          done();
        });
    });

    it('reload should re-fetch teams using current active filters', () => {
      const filters = { quarterId: 99 };
      service.loadTeams(filters);
      teamServiceMock.getAllTeams.mockClear();

      service.reload();

      expect(teamServiceMock.getAllTeams)
        .toHaveBeenCalledWith(filters);
    });
  });

  describe('Mutations & Automated Refresh Cycles', () => {
    it('createTeam should call api client and trigger a state reload', () => {
      const spyReload = jest.spyOn(service, 'reload');
      const newTeam = { name: 'Gamma Team' } as Team;

      service.createTeam(newTeam)
        .subscribe();

      expect(teamServiceMock.createTeam)
        .toHaveBeenCalledWith(newTeam);
      expect(spyReload)
        .toHaveBeenCalledTimes(1);
    });

    it('updateTeam should call api client and trigger a state reload', () => {
      const spyReload = jest.spyOn(service, 'reload');
      const targetTeam = mockTeams[0];

      service.updateTeam(targetTeam)
        .subscribe();

      expect(teamServiceMock.updateTeam)
        .toHaveBeenCalledWith(targetTeam);
      expect(spyReload)
        .toHaveBeenCalledTimes(1);
    });

    it('deleteTeam should call api client and trigger a state reload', () => {
      const spyReload = jest.spyOn(service, 'reload');

      service.deleteTeam(1)
        .subscribe();

      expect(teamServiceMock.deleteTeam)
        .toHaveBeenCalledWith(1);
      expect(spyReload)
        .toHaveBeenCalledTimes(1);
    });

    it('addUsersToTeam should call api client and trigger a state reload', () => {
      const spyReload = jest.spyOn(service, 'reload');
      const mockUsers = [{ id: 10 } as User];

      service.addUsersToTeam(mockTeams[0], mockUsers)
        .subscribe();

      expect(teamServiceMock.addUsersToTeam)
        .toHaveBeenCalledWith(mockTeams[0], mockUsers);
      expect(spyReload)
        .toHaveBeenCalledTimes(1);
    });

    it('removeUserFromTeam should call api client and trigger a state reload', () => {
      const spyReload = jest.spyOn(service, 'reload');

      service.removeUserFromTeam(10, mockTeams[0])
        .subscribe();

      expect(teamServiceMock.removeUserFromTeam)
        .toHaveBeenCalledWith(10, mockTeams[0]);
      expect(spyReload)
        .toHaveBeenCalledTimes(1);
    });

    it('updateOrAddTeamMembership should call api client and trigger a state reload', () => {
      const spyReload = jest.spyOn(service, 'reload');
      const userTeam = { isTeamAdmin: true,
        team: mockTeams[0] } as UserTeam;

      service.updateOrAddTeamMembership(10, userTeam)
        .subscribe();

      expect(teamServiceMock.updateOrAddTeamMembership)
        .toHaveBeenCalledWith(10, userTeam);
      expect(spyReload)
        .toHaveBeenCalledTimes(1);
    });
  });

  describe('Cross-Service Side Effects (Archive Paths)', () => {
    it('archiveTeam should save changes, reload team state, and tell UserService to refresh users', () => {
      const spyReload = jest.spyOn(service, 'reload');
      const targetTeam = { ...mockTeams[0],
        markedAsArchivedAt: new Date() };

      service.archiveTeam(targetTeam)
        .subscribe();

      expect(teamServiceMock.archiveTeam)
        .toHaveBeenCalledWith(targetTeam);
      expect(spyReload)
        .toHaveBeenCalledTimes(1);
      expect(userServiceMock.reloadUsers)
        .toHaveBeenCalledTimes(1);
    });

    it('unarchiveTeam should save changes, reload team state, and tell UserService to refresh users', () => {
      const spyReload = jest.spyOn(service, 'reload');

      service.unarchiveTeam(1)
        .subscribe();

      expect(teamServiceMock.unarchiveTeam)
        .toHaveBeenCalledWith(1);
      expect(spyReload)
        .toHaveBeenCalledTimes(1);
      expect(userServiceMock.reloadUsers)
        .toHaveBeenCalledTimes(1);
    });
  });
});
