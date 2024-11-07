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
      providers: [TranslateService, provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ObjectiveMenuActionsService);

    specificMenuEntriesSpy = jest.spyOn(service as any, 'getSpecificMenuEntries');
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getMenu', () => {
    it('should return default and specific menu entries for an ongoing objective', () => {
      let spyOn = jest.spyOn(service as any, 'getOngoingMenuActions');

      let objectiveMinLocal: ObjectiveMin = objectiveMin;
      objectiveMinLocal.state = State.ONGOING;
      service.getMenu(objectiveMinLocal);
      expect(spyOn).toHaveBeenCalledTimes(1);
    });

    it('should return draft menu entries for a draft objective', () => {
      let spyOn = jest.spyOn(service as any, 'getDraftMenuActions');

      let objectiveMinLocal: ObjectiveMin = objectiveMin;
      objectiveMinLocal.state = State.DRAFT;
      service.getMenu(objectiveMinLocal);
      expect(spyOn).toHaveBeenCalledTimes(1);
    });

    it('should return completed menu entries for a successful objective', () => {
      let spyOn = jest.spyOn(service as any, 'getCompletedMenuActions');

      let objectiveMinLocal: ObjectiveMin = objectiveMin;
      objectiveMinLocal.state = State.SUCCESSFUL;
      service.getMenu(objectiveMinLocal);
      expect(spyOn).toHaveBeenCalledTimes(1);
    });

    it('should return completed menu entries for a non-successful objective', () => {
      let spyOn = jest.spyOn(service as any, 'getCompletedMenuActions');

      let objectiveMinLocal: ObjectiveMin = objectiveMin;
      objectiveMinLocal.state = State.NOTSUCCESSFUL;
      service.getMenu(objectiveMinLocal);
      expect(spyOn).toHaveBeenCalledTimes(1);
    });
    afterEach(() => {
      expect(specificMenuEntriesSpy).toHaveBeenCalledTimes(1);
    });
  });

  describe('getReleaseAction', () => {
    it('should return release from backlog action for an objective in backlog quarter', () => {
      jest.spyOn(service as any, 'isInBacklogQuarter').mockReturnValue(true);
      let spyOn = jest.spyOn(service as any, 'isInBacklogQuarter').mockReturnValue(true);
      // @ts-expect-error
      service.getReleaseAction(objectiveMin);
      expect(spyOn).toHaveBeenCalledTimes(1);
    });

    it('should return release from quarter action for an objective in non-backlog quarter', () => {
      let spyOn = jest.spyOn(service as any, 'isInBacklogQuarter').mockReturnValue(false);
      // @ts-expect-error
      service.getReleaseAction(objectiveMin);
      expect(spyOn).toHaveBeenCalledTimes(1);
    });
  });
});
