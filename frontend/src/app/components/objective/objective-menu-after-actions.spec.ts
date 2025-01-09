import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { ObjectiveService } from '../../services/objective.service';
import { CompletedService } from '../../services/completed.servce';
import { RefreshDataService } from '../../services/refresh-data.service';
import { ObjectiveMenuAfterActions } from './objective-menu-after-actions';
import { objective, objectiveMin } from '../../shared/test-data';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { State } from '../../shared/types/enums/state';
import { Completed } from '../../shared/types/model/completed';

describe('ObjectiveMenuAfterActions', () => {
  let objectiveMenuAfterActions: ObjectiveMenuAfterActions;
  let objectiveService: ObjectiveService;
  let completedService: CompletedService;
  let refreshDataService: RefreshDataService;

  const result = { endState: 'SUCCESSFUL',
    comment: 'Well done',
    objective: objective };

  const completed: Completed = {
    id: null,
    version: objectiveMin.version,
    objective: objective,
    comment: result.comment
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting()]
    });

    objectiveService = TestBed.inject(ObjectiveService);
    completedService = TestBed.inject(CompletedService);
    refreshDataService = TestBed.inject(RefreshDataService);
    objectiveMenuAfterActions = new ObjectiveMenuAfterActions(objectiveService, completedService, refreshDataService);

    jest.spyOn(objectiveService, 'updateObjective');
    jest.spyOn(objectiveService, 'deleteObjective');
    jest.spyOn(completedService, 'createCompleted');
    jest.spyOn(completedService, 'deleteCompleted');
    jest.spyOn(refreshDataService, 'markDataRefresh');
  });

  it('should complete an objective and mark data refresh', () => {
    const updatedObjective = { ...objective,
      state: State.SUCCESSFUL };


    jest.spyOn(objectiveService, 'getFullObjective')
      .mockReturnValue(of(objective));
    jest.spyOn(objectiveService, 'updateObjective')
      .mockReturnValue(of(updatedObjective));
    jest.spyOn(completedService, 'createCompleted')
      .mockReturnValue(of(completed));

    objectiveMenuAfterActions.completeObjective(objectiveMin, result);

    expect(objectiveService.getFullObjective)
      .toHaveBeenCalledWith(101);
    expect(objectiveService.updateObjective)
      .toHaveBeenCalledWith({ ...updatedObjective,
        state: 'SUCCESSFUL' });
    expect(completedService.createCompleted)
      .toHaveBeenCalledWith({
        id: null,
        version: 1,
        objective: { ...updatedObjective,
          state: 'SUCCESSFUL' },
        comment: 'Well done'
      });
    expect(refreshDataService.markDataRefresh)
      .toHaveBeenCalled();
  });

  it('should release an objective from the quarter and mark data refresh', () => {
    const updatedObjective = { ...objective,
      state: State.ONGOING };

    jest.spyOn(objectiveService, 'getFullObjective')
      .mockReturnValue(of(objective));
    jest.spyOn(objectiveService, 'updateObjective')
      .mockReturnValue(of(updatedObjective));

    objectiveMenuAfterActions.releaseFromQuarter(objectiveMin);

    expect(objectiveService.getFullObjective)
      .toHaveBeenCalledWith(101);
    expect(objectiveService.updateObjective)
      .toHaveBeenCalledWith({ ...objective,
        state: 'ONGOING' });
    expect(refreshDataService.markDataRefresh)
      .toHaveBeenCalled();
  });

  it('should delete an objective and mark data refresh', () => {
    jest.spyOn(objectiveService, 'deleteObjective')
      .mockReturnValue(of(objective));

    objectiveMenuAfterActions.deleteObjective(objectiveMin);

    expect(objectiveService.deleteObjective)
      .toHaveBeenCalledWith(101);
    expect(refreshDataService.markDataRefresh)
      .toHaveBeenCalled();
  });

  it('should move an objective back to draft and mark data refresh', () => {
    const updatedObjective = { ...objective,
      state: State.DRAFT };

    jest.spyOn(objectiveService, 'getFullObjective')
      .mockReturnValue(of(objective));
    jest.spyOn(objectiveService, 'updateObjective')
      .mockReturnValue(of(updatedObjective));

    objectiveMenuAfterActions.objectiveBackToDraft(objectiveMin);

    expect(objectiveService.getFullObjective)
      .toHaveBeenCalledWith(101);
    expect(objectiveService.updateObjective)
      .toHaveBeenCalledWith({ ...objective,
        state: 'DRAFT' });
    expect(refreshDataService.markDataRefresh)
      .toHaveBeenCalled();
  });

  it('should reopen an objective, delete completion, and mark data refresh', () => {
    const updatedObjective = { ...objective,
      state: State.ONGOING };

    jest.spyOn(objectiveService, 'getFullObjective')
      .mockReturnValue(of(objective));
    jest.spyOn(objectiveService, 'updateObjective')
      .mockReturnValue(of(updatedObjective));
    jest.spyOn(completedService, 'deleteCompleted')
      .mockReturnValue(of(completed));

    objectiveMenuAfterActions.objectiveReopen(objectiveMin);

    expect(objectiveService.getFullObjective)
      .toHaveBeenCalledWith(101);
    expect(objectiveService.updateObjective)
      .toHaveBeenCalledWith({ ...objective,
        state: 'ONGOING' });
    expect(completedService.deleteCompleted)
      .toHaveBeenCalledWith(5);
    expect(refreshDataService.markDataRefresh)
      .toHaveBeenCalled();
  });
});
