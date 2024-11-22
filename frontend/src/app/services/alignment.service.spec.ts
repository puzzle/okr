import { TestBed } from '@angular/core/testing';

import { AlignmentService } from './alignment.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { alignmentLists } from '../shared/testData';

const httpClient = {
  get: jest.fn(),
};

describe('AlignmentService', () => {
  let service: AlignmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [{ provide: HttpClient, useValue: httpClient }],
    }).compileComponents();
    service = TestBed.inject(AlignmentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set params of filter correctly without objectiveQuery', (done) => {
    jest.spyOn(httpClient, 'get').mockReturnValue(of(alignmentLists));
    service.getAlignmentByFilter(2, [4, 5], '').subscribe((alignmentLists) => {
      alignmentLists.alignmentObjectDtoList.forEach((object) => {
        expect(object.objectType).toEqual('objective');
        expect(object.objectTeamName).toEqual('Example Team');
      });
      alignmentLists.alignmentConnectionDtoList.forEach((connection) => {
        expect(connection.targetKeyResultId).toEqual(null);
        expect(connection.alignedObjectiveId).toEqual(1);
        expect(connection.targetObjectiveId).toEqual(2);
      });
      done();
    });
    expect(httpClient.get).toHaveBeenCalledWith('/api/v2/alignments/alignmentLists', {
      params: { quarterFilter: 2, teamFilter: [4, 5] },
    });
  });

  it('should set params of filter correctly with objectiveQuery', (done) => {
    jest.spyOn(httpClient, 'get').mockReturnValue(of(alignmentLists));
    service.getAlignmentByFilter(2, [4, 5], 'objective').subscribe((alignmentLists) => {
      alignmentLists.alignmentObjectDtoList.forEach((object) => {
        expect(object.objectType).toEqual('objective');
        expect(object.objectTeamName).toEqual('Example Team');
      });
      alignmentLists.alignmentConnectionDtoList.forEach((connection) => {
        expect(connection.targetKeyResultId).toEqual(null);
        expect(connection.alignedObjectiveId).toEqual(1);
        expect(connection.targetObjectiveId).toEqual(2);
      });
      done();
    });
    expect(httpClient.get).toHaveBeenCalledWith('/api/v2/alignments/alignmentLists', {
      params: { objectiveQuery: 'objective', quarterFilter: 2, teamFilter: [4, 5] },
    });
  });
});
