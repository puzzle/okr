import { TestBed } from '@angular/core/testing';

import { KeyResultMeasure, KeyResultService } from './key-result.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

const response = [
  {
    id: 1,
    objectiveId: 1,
    title: 'Keyresult 1',
    description:
      "This is the description of Keyresult 1: Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
    ownerId: 1,
    ownerFirstname: 'Alice',
    ownerLastname: 'Wunderland',
    quarterId: 1,
    quarterLabel: 'GJ 22/23-Q1',
    expectedEvolution: 'INCREASE',
    unit: 'PERCENT',
    basicValue: 0,
    targetValue: 100,
    measure: {
      id: 1,
      keyResultId: 1,
      value: 60,
      changeInfo:
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
      initiatives: '',
      createdById: 1,
      createdOn: '2022-01-01T00:00:00',
    },
  },
  {
    id: 2,
    objectiveId: 1,
    title: 'Keyresult 2',
    description:
      "This is the description of Keyresult 2: Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
    ownerId: 1,
    ownerFirstname: 'Alice',
    ownerLastname: 'Wunderland',
    quarterId: 1,
    quarterLabel: 'GJ 22/23-Q1',
    expectedEvolution: 'DECREASE',
    unit: 'CHF',
    basicValue: 10,
    targetValue: 50,
    measure: {
      id: 4,
      keyResultId: 2,
      value: 15,
      changeInfo:
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
      initiatives: '',
      createdById: 1,
      createdOn: '2023-01-01T00:00:00',
    },
  },
];

describe('KeyResultService', () => {
  let service: KeyResultService;
  let httpTestingController: HttpTestingController;
  const URL = '/api/v1/objectives';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(KeyResultService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  test('should be created', () => {
    expect(service).toBeTruthy();
  });

  test('should get KeyResults of Objective', (done) => {
    service.getKeyResultsOfObjective(42).subscribe({
      next(response: KeyResultMeasure[]) {
        expect(response.length).toBe(2);
        done();
      },
      error(error) {
        done(error);
      },
    });

    const req = httpTestingController.expectOne(`${URL}/42/keyresults`);
    expect(req.request.method).toEqual('GET');
    req.flush(response);
    httpTestingController.verify();
  });

  test('should throw a error if load failed', (done) => {
    service.getKeyResultsOfObjective(42).subscribe({
      next() {
        done('should have a 404 Not Found');
      },
      error() {
        done();
      },
    });
    const req = httpTestingController.expectOne(`${URL}/42/keyresults`);
    expect(req.request.method).toEqual('GET');
    req.flush(null, { status: 404, statusText: 'Not Found' });
    httpTestingController.verify();
  });
});
