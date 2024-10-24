import { ChangeDetectionStrategy, Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Quarter } from '../../types/model/Quarter';
import { TeamService } from '../../../services/team.service';
import { Team } from '../../types/model/Team';
import { QuarterService } from '../../../services/quarter.service';
import { forkJoin, Observable, of, Subject, takeUntil } from 'rxjs';
import { ObjectiveService } from '../../../services/objective.service';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { State } from '../../types/enums/State';
import { ObjectiveMin } from '../../types/model/ObjectiveMin';
import { Objective } from '../../types/model/Objective';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { formInputCheck, getQuarterLabel, getValueFromQuery, hasFormFieldErrors, isMobileDevice } from '../../common';
import { ActivatedRoute } from '@angular/router';
import { CONFIRM_DIALOG_WIDTH, GJ_REGEX_PATTERN } from '../../constantLibary';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-objective-form',
  templateUrl: './objective-form.component.html',
  styleUrls: ['./objective-form.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveFormComponent implements OnInit, OnDestroy {
  objectiveForm = new FormGroup({
    title: new FormControl<string>('', [Validators.required, Validators.minLength(2), Validators.maxLength(250)]),
    description: new FormControl<string>('', [Validators.maxLength(4096)]),
    quarter: new FormControl<number>(0, [Validators.required]),
    team: new FormControl<number>({ value: 0, disabled: true }, [Validators.required]),
    relation: new FormControl<number>({ value: 0, disabled: true }),
    createKeyResults: new FormControl<boolean>(false),
  });
  quarters$: Observable<Quarter[]> = of([]);
  quarters: Quarter[] = [];
  teams$: Observable<Team[]> = of([]);
  currentTeam: Subject<Team> = new Subject<Team>();
  state: string | null = null;
  version!: number;
  protected readonly formInputCheck = formInputCheck;
  protected readonly hasFormFieldErrors = hasFormFieldErrors;
  private unsubscribe$ = new Subject<void>();

  constructor(
    private route: ActivatedRoute,
    private teamService: TeamService,
    private quarterService: QuarterService,
    private objectiveService: ObjectiveService,
    public dialogRef: MatDialogRef<ObjectiveFormComponent>,
    private dialog: MatDialog,
    @Inject(MAT_DIALOG_DATA)
    public data: {
      action: string;
      objective: {
        objectiveId?: number;
        teamId?: number;
      };
    },
    private translate: TranslateService,
  ) {}

  onSubmit(submitType: any): void {
    const value = this.objectiveForm.getRawValue();
    const state = this.data.objective.objectiveId == null ? submitType : this.state;
    let objectiveDTO: Objective = {
      id: this.data.objective.objectiveId,
      version: this.version,
      quarterId: value.quarter,
      description: value.description,
      title: value.title,
      teamId: value.team,
      state: state,
    } as unknown as Objective;

    const submitFunction = this.getSubmitFunction(objectiveDTO.id, objectiveDTO);
    submitFunction.subscribe((savedObjective: Objective) =>
      this.closeDialog(savedObjective, false, value.createKeyResults!),
    );
  }

  ngOnInit(): void {
    const isCreating: boolean = !!this.data.objective.objectiveId;
    this.teams$ = this.teamService.getAllTeams().pipe(takeUntil(this.unsubscribe$));
    this.quarters$ = this.quarterService.getAllQuarters();
    const objective$ = isCreating
      ? this.objectiveService.getFullObjective(this.data.objective.objectiveId!)
      : of(this.getDefaultObjective());

    forkJoin([objective$, this.quarters$]).subscribe(([objective, quarters]) => {
      this.quarters = quarters;
      const teamId = isCreating ? objective.teamId : this.data.objective.teamId;
      let quarterId = getValueFromQuery(this.route.snapshot.queryParams['quarter'], quarters[1].id)[0];

      let currentQuarter: Quarter | undefined = this.quarters.find((quarter) => quarter.id == quarterId);
      if (currentQuarter && !this.isBacklogQuarter(currentQuarter.label) && this.data.action == 'releaseBacklog') {
        quarterId = quarters[1].id;
      }

      this.state = objective.state;
      this.version = objective.version;
      this.teams$.subscribe((value) => {
        this.currentTeam.next(value.filter((team) => team.id == teamId)[0]);
      });

      this.objectiveForm.patchValue({
        title: objective.title,
        description: objective.description,
        team: teamId,
        quarter: quarterId,
      });
    });
  }

  ngOnDestroy() {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  getSubmitFunction(id: number, objectiveDTO: any): Observable<Objective> {
    if (this.data.action == 'duplicate') {
      objectiveDTO.id = null;
      objectiveDTO.state = 'DRAFT' as State;
      return this.objectiveService.duplicateObjective(id, objectiveDTO);
    } else {
      if (this.data.action == 'releaseBacklog') objectiveDTO.state = 'ONGOING' as State;
      return id
        ? this.objectiveService.updateObjective(objectiveDTO)
        : this.objectiveService.createObjective(objectiveDTO);
    }
  }

  deleteObjective() {
    const dialogConfig = isMobileDevice()
      ? {
          maxWidth: '100vw',
          maxHeight: '100vh',
          height: '100vh',
          width: '100vw',
        }
      : {
          width: CONFIRM_DIALOG_WIDTH,
          height: 'auto',
        };
    const dialog = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Objective',
      },
      width: dialogConfig.width,
      height: dialogConfig.height,
      maxHeight: dialogConfig.maxHeight,
      maxWidth: dialogConfig.maxWidth,
    });
    dialog.afterClosed().subscribe((result) => {
      if (result) {
        this.objectiveService.deleteObjective(this.data.objective.objectiveId!).subscribe({
          next: () => {
            let objectiveDTO: Objective = { id: this.data.objective.objectiveId! } as unknown as Objective;
            this.closeDialog(objectiveDTO, true);
          },
          error: () => {
            this.dialogRef.close();
          },
        });
      }
    });
  }

  objectiveToObjectiveMin(objectiveDto: Objective): ObjectiveMin {
    return {
      ...objectiveDto,
      state: State[objectiveDto.state as string as keyof typeof State],
    } as unknown as ObjectiveMin;
  }

  closeDialog(objectiveDTO: Objective, willDelete: boolean = false, addKeyResult: boolean = false) {
    const value = this.objectiveForm.getRawValue();
    const objectiveMin: ObjectiveMin = this.objectiveToObjectiveMin(objectiveDTO);
    this.dialogRef.close({
      objective: objectiveMin,
      teamId: value.team,
      delete: willDelete,
      addKeyResult: addKeyResult,
    });
  }

  getErrorMessage(error: string, field: string, firstNumber: number | null, secondNumber: number | null): string {
    return field + this.translate.instant('DIALOG_ERRORS.' + error).format(firstNumber, secondNumber);
  }

  getDefaultObjective() {
    return {
      id: 0,
      title: '',
      description: '',
      state: 'DRAFT' as State,
      teamId: 0,
      quarterId: 0,
    } as Objective;
  }

  allowedToSaveBacklog() {
    let currentQuarter: Quarter | undefined = this.quarters.find(
      (quarter) => quarter.id == this.objectiveForm.value.quarter,
    );
    if (currentQuarter) {
      let isBacklogCurrent: boolean = !this.isBacklogQuarter(currentQuarter.label);
      if (this.data.action == 'duplicate') return true;
      if (this.data.objective.objectiveId) {
        return isBacklogCurrent ? this.state == 'DRAFT' : true;
      } else {
        return !isBacklogCurrent;
      }
    } else {
      return true;
    }
  }

  allowedOption(quarter: Quarter) {
    if (quarter.label == 'Backlog') {
      if (this.data.action == 'duplicate') {
        return true;
      } else if (this.data.action == 'releaseBacklog') {
        return false;
      } else if (this.data.objective.objectiveId) {
        return this.state == 'DRAFT';
      } else {
        return true;
      }
    } else {
      return true;
    }
  }

  isBacklogQuarter(label: string) {
    return GJ_REGEX_PATTERN.test(label);
  }

  protected readonly getQuarterLabel = getQuarterLabel;
}
