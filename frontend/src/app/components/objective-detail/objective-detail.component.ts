import { ChangeDetectionStrategy, Component, OnInit } from "@angular/core";
import { Objective } from "../../shared/types/model/Objective";
import { ObjectiveService } from "../../services/objective.service";
import { BehaviorSubject, catchError, EMPTY } from "rxjs";
import { RefreshDataService } from "../../services/refresh-data.service";
import { KeyresultDialogComponent } from "../keyresult-dialog/keyresult-dialog.component";
import { ObjectiveFormComponent } from "../../shared/dialog/objective-dialog/objective-form.component";
import { ActivatedRoute, Router } from "@angular/router";
import { DialogService } from "../../services/dialog.service";

@Component({
  selector: "app-objective-detail",
  templateUrl: "./objective-detail.component.html",
  styleUrl: "objective-detail.component.scss",
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ObjectiveDetailComponent implements OnInit {
  objectiveId!: number;

  objective$: BehaviorSubject<Objective> = new BehaviorSubject<Objective>({} as Objective);

  constructor(
    private objectiveService: ObjectiveService,
    private dialogService: DialogService,
    private refreshDataService: RefreshDataService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.objectiveId = this.getIdFromParams();
    this.loadObjective(this.objectiveId);
  }

  private getIdFromParams(): number {
    const id = this.route.snapshot.paramMap.get("id");
    if (!id) {
      throw Error("objective id is undefined");
    }
    return parseInt(id);
  }

  loadObjective(id: number): void {
    this.objectiveService
      .getFullObjective(id)
      .pipe(catchError(() => EMPTY))
      .subscribe((objective) => this.objective$.next(objective));
  }

  openAddKeyResultDialog() {
    this.dialogService
      .open(KeyresultDialogComponent, {
        data: {
          objective: this.objective$.getValue(),
          keyResult: null
        }
      })
      .afterClosed()
      .subscribe((result) => {
        if (result?.openNew) {
          this.openAddKeyResultDialog();
        }
        this.refreshDataService.markDataRefresh();
      });
  }

  openEditObjectiveDialog() {
    this.dialogService
      .open(ObjectiveFormComponent, {
        data: {
          objective: {
            objectiveId: this.objective$.getValue().id,
            teamId: this.objective$.value.teamId
          }
        }
      })
      .afterClosed()
      .subscribe((result) => {
        this.refreshDataService.markDataRefresh();
        if (result.delete) {
          this.backToOverview();
        } else {
          this.loadObjective(this.objective$.value.id);
        }
      });
  }

  backToOverview() {
    this.router.navigate([""]);
  }
}
