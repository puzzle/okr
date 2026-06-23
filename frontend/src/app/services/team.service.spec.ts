import { TestBed } from '@angular/core/testing';
import { TeamService } from './team.service';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { team1 } from '../shared/test-data';

describe('TeamService', () => {
  let service: TeamService;
  let httpTestingController: HttpTestingController;
  const API_URL = '/api/v2/teams';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(),
        provideHttpClientTesting()]
    });
    service = TestBed.inject(TeamService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  describe('archiveTeam', () => {
    it('should send PUT request with correct archive payload', () => {
      const archiveDate = new Date('2022-01-01');
      const copyOfTeam = { ...team1,
        markedAsArchivedAt: archiveDate };

      service.archiveTeam(copyOfTeam)
        .subscribe();

      const req = httpTestingController.expectOne(`${API_URL}/${copyOfTeam.id}/archive`);
      expect(req.request.method)
        .toBe('PUT');
      expect(req.request.body)
        .toEqual({ markedAsArchivedAt: archiveDate });

      req.flush(null);
    });
  });

  describe('unarchiveTeam', () => {
    it('should send unarchive PUT request with null body', () => {
      service.unarchiveTeam(team1.id)
        .subscribe();

      const req = httpTestingController.expectOne(`${API_URL}/${team1.id}/unarchive`);
      expect(req.request.method)
        .toBe('PUT');
      expect(req.request.body)
        .toEqual({});

      req.flush(null);
    });
  });
});
