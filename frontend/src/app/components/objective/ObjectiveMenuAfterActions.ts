import { Objective } from '../../shared/types/model/objective';
import { State } from '../../shared/types/enums/state';
import { Completed } from '../../shared/types/model/completed';
import { ObjectiveService } from '../../services/objective.service';
import { RefreshDataService } from '../../services/refresh-data.service';
import { ObjectiveMin } from '../../shared/types/model/objective-min';
import { CompletedService } from '../../services/completed.servce';

export class ObjectiveMenuAfterActions {
  constructor(private readonly objectiveService: ObjectiveService,
    private readonly completedService: CompletedService,
    private readonly refreshDataService: RefreshDataService) {}

  completeObjective(objectiveMin: ObjectiveMin, result: { endState: string;
    comment: string | null;
    objective: any; }) {
    this.objectiveService.getFullObjective(objectiveMin.id)
      .subscribe((objective: Objective) => {
        objective.state = result.endState as State;
        const completed: Completed = {
          id: null,
          version: objectiveMin.version,
          objective: objective,
          comment: result.comment
        };
        this.objectiveService.updateObjective(objective)
          .subscribe(() => {
            this.completedService.createCompleted(completed)
              .subscribe(() => {
                this.refreshDataService.markDataRefresh();
              });
          });
      });
  }

  releaseFromQuarter(objectiveMin: ObjectiveMin) {
    this.objectiveService.getFullObjective(objectiveMin.id)
      .subscribe((objective: Objective) => {
        objective.state = 'ONGOING' as State;
        this.objectiveService.updateObjective(objective)
          .subscribe(() => {
            this.refreshDataService.markDataRefresh();
          });
      });
  }

  deleteObjective(objective: ObjectiveMin) {
    this.objectiveService.deleteObjective(objective.id).subscribe(() => {
      this.refreshDataService.markDataRefresh();
    });
  }

  objectiveBackToDraft(objectiveMin: ObjectiveMin) {
    this.objectiveService.getFullObjective(objectiveMin.id)
      .subscribe((objective: Objective) => {
        objective.state = 'DRAFT' as State;
        this.objectiveService.updateObjective(objective)
          .subscribe(() => {
            this.refreshDataService.markDataRefresh();
          });
      });
  }

  objectiveReopen(objectiveMin: ObjectiveMin) {
    this.objectiveService.getFullObjective(objectiveMin.id)
      .subscribe((objective: Objective) => {
        objective.state = 'ONGOING' as State;
        this.objectiveService.updateObjective(objective)
          .subscribe(() => {
            this.completedService.deleteCompleted(objective.id)
              .subscribe(() => {
                this.refreshDataService.markDataRefresh();
              });
          });
      });
  }
}
