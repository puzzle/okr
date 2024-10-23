import { Objective } from '../../shared/types/model/Objective';
import { State } from '../../shared/types/enums/State';
import { Completed } from '../../shared/types/model/Completed';
import { ObjectiveService } from '../../services/objective.service';
import { RefreshDataService } from '../../services/refresh-data.service';

export class ObjectiveMenuAfterActions {
  constructor(
    private readonly objectiveService: ObjectiveService,
    private readonly refreshDataService: RefreshDataService,
  ) {}

  completeObjective(objective: Objective, result: { endState: string; comment: string | null; objective: any }) {
    objective.state = result.endState as State;
    const completed: Completed = {
      id: null,
      version: objective.version,
      objective: objective,
      comment: result.comment,
    };
    this.objectiveService.updateObjective(objective).subscribe(() => {
      this.objectiveService.createCompleted(completed).subscribe(() => {
        this.refreshDataService.markDataRefresh();
      });
    });
  }

  releaseFromQuarter(objective: Objective) {
    objective.state = 'ONGOING' as State;
    this.objectiveService.updateObjective(objective).subscribe(() => {
      this.refreshDataService.markDataRefresh();
    });
  }

  objectiveBackToDraft(objective: Objective) {
    objective.state = 'DRAFT' as State;
    this.objectiveService.updateObjective(objective).subscribe(() => {
      this.refreshDataService.markDataRefresh();
    });
  }

  objectiveReopen(objective: Objective) {
    objective.state = 'ONGOING' as State;
    this.objectiveService.updateObjective(objective).subscribe(() => {
      this.objectiveService.deleteCompleted(objective.id).subscribe(() => {
        this.refreshDataService.markDataRefresh();
      });
    });
  }
}
