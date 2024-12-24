import { TestBed } from '@angular/core/testing';

import { ObjectiveMenuActionsService } from './objective-menu-actions.service';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ObjectiveMin } from '../shared/types/model/ObjectiveMin';
import { State } from '../shared/types/enums/State';
import { objectiveMin } from '../shared/testData';

describe('ObjectiveMenuActionsService', () => {
  let service: ObjectiveMenuActionsService;
  let specificMenuEntriesSpy: jest.SpyInstance;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      providers: [
        TranslateService,
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(ObjectiveMenuActionsService);

    specificMenuEntriesSpy = jest.spyOn(service as any, 'getSpecificMenuEntries');
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  describe('getMenu', () => {
    afterEach(() => {
      expect(specificMenuEntriesSpy)
        .toHaveBeenCalledTimes(1);
    });

    it('should return default and specific menu entries for an ongoing objective', () => {
      const spyOn = jest.spyOn(service as any, 'getOngoingMenuActions');

      const ongoingObjectiveMin: ObjectiveMin = objectiveMin;
      ongoingObjectiveMin.state = State.ONGOING;
      service.getMenu(ongoingObjectiveMin);
      expect(spyOn)
        .toHaveBeenCalledTimes(1);
    });

    it('should return draft menu entries for a draft objective', () => {
      const spyOn = jest.spyOn(service as any, 'getDraftMenuActions');

      const draftObjectiveMin: ObjectiveMin = objectiveMin;
      draftObjectiveMin.state = State.DRAFT;
      service.getMenu(draftObjectiveMin);
      expect(spyOn)
        .toHaveBeenCalledTimes(1);
    });

    it('should return completed menu entries for a successful objective', () => {
      const spyOn = jest.spyOn(service as any, 'getCompletedMenuActions');

      const successfulObjectiveMin: ObjectiveMin = objectiveMin;
      successfulObjectiveMin.state = State.SUCCESSFUL;
      service.getMenu(successfulObjectiveMin);
      expect(spyOn)
        .toHaveBeenCalledTimes(1);
    });

    it('should return completed menu entries for a non-successful objective', () => {
      const spyOn = jest.spyOn(service as any, 'getCompletedMenuActions');

      const notSuccessfulObjectiveMinLocal: ObjectiveMin = objectiveMin;
      notSuccessfulObjectiveMinLocal.state = State.NOTSUCCESSFUL;
      service.getMenu(notSuccessfulObjectiveMinLocal);
      expect(spyOn)
        .toHaveBeenCalledTimes(1);
    });
  });

  describe('get correct release action', () => {
    it('should return release from backlog action for an objective in the backlog quarter', () => {
      jest.spyOn(service as any, 'isInBacklogQuarter')
        .mockReturnValue(true);
      const spyOn = jest.spyOn(service as any, 'isInBacklogQuarter')
        .mockReturnValue(true);
      // @ts-expect-error
      service.getReleaseAction(objectiveMin);
      expect(spyOn)
        .toHaveBeenCalledTimes(1);
    });

    it('should return release from quarter action for an objective in a non-backlog quarter', () => {
      const spyOn = jest.spyOn(service as any, 'isInBacklogQuarter')
        .mockReturnValue(false);
      // @ts-expect-error
      service.getReleaseAction(objectiveMin);
      expect(spyOn)
        .toHaveBeenCalledTimes(1);
    });
  });
});
