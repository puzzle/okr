import { TestBed } from '@angular/core/testing';

import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { Overview, OverviewService } from './overview.service';

const response = [
  {
    team: {
      id: 1,
      name: 'Team 1',
    },
    objectives: {
      id: 1,
      title: 'Objective 1',
      ownerId: 1,
      ownerFirstname: 'Alice',
      ownerLastname: 'Wunderland',
      teamId: 1,
      teamName: 'Team 1',
      quarterId: 1,
      quarterLabel: 'GJ 22/23-Q1',
      description: 'This is the description of Objective 1',
      progress: 20,
    },
  },
  {
    team: {
      id: 2,
      name: 'Team 2',
    },
    objectives: {
      id: 2,
      title: 'Objective 2',
      ownerId: 1,
      ownerFirstname: 'Alice',
      ownerLastname: 'Wunderland',
      teamId: 1,
      teamName: 'Team 1',
      quarterId: 1,
      quarterLabel: 'GJ 22/23-Q1',
      description: 'This is the description of Objective 1',
      progress: 20,
    },
  },
];

describe('OverviewService', () => {
  let overviewService: OverviewService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    overviewService = TestBed.inject(OverviewService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  test('should be created', () => {
    expect(overviewService).toBeTruthy();
  });

  test('should get team with their objectives', (done) => {
    overviewService.getOverview().subscribe({
      next(response: Overview[]) {
        expect(response.length).toBe(2);
        done();
      },
      error(error) {
        done(error);
      },
    });

    const request = httpTestingController.expectOne(`api/v1/overview`);
    expect(request.request.method).toEqual('GET');
    request.flush(response);
    httpTestingController.verify();
  });
});
