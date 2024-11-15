import { Objective } from '../../shared/types/model/Objective';
import { State } from '../../shared/types/enums/State';
import { Completed } from '../../shared/types/model/Completed';
import { ObjectiveService } from '../../services/objective.service';
import { RefreshDataService } from '../../services/refresh-data.service';
import { ObjectiveMin } from '../../shared/types/model/ObjectiveMin';

export class ObjectiveMenuAfterActions {
  constructor(
    private readonly objectiveService: ObjectiveService,
    private readonly refreshDataService: RefreshDataService,
  ) {}

  completeObjective(objectiveMin: ObjectiveMin, result: { endState: string; comment: string | null; objective: any }) {
    this.objectiveService.getFullObjective(objectiveMin.id).subscribe((objective: Objective) => {
      objective.state = result.endState as State;
      const completed: Completed = {
        id: null,
        version: objectiveMin.version,
        objective: objective,
        comment: result.comment,
      };
      this.objectiveService.updateObjective(objective).subscribe(() => {
        this.objectiveService.createCompleted(completed).subscribe(() => {
          this.refreshDataService.markDataRefresh();
        });
      });
    });
  }

  releaseFromQuarter(objectiveMin: ObjectiveMin) {
    this.objectiveService.getFullObjective(objectiveMin.id).subscribe((objective: Objective) => {
      objective.state = 'ONGOING' as State;
      this.objectiveService.updateObjective(objective).subscribe(() => {
        this.refreshDataService.markDataRefresh();
      });
    });
  }

  objectiveBackToDraft(objectiveMin: ObjectiveMin) {
    this.objectiveService.getFullObjective(objectiveMin.id).subscribe((objective: Objective) => {
      objective.state = 'DRAFT' as State;
      this.objectiveService.updateObjective(objective).subscribe(() => {
        this.refreshDataService.markDataRefresh();
      });
    });
  }

  objectiveReopen(objectiveMin: ObjectiveMin) {
    this.objectiveService.getFullObjective(objectiveMin.id).subscribe((objective: Objective) => {
      objective.state = 'ONGOING' as State;
      this.objectiveService.updateObjective(objective).subscribe(() => {
        this.objectiveService.deleteCompleted(objective.id).subscribe(() => {
          this.refreshDataService.markDataRefresh();
        });
      });
    });
  }
}
