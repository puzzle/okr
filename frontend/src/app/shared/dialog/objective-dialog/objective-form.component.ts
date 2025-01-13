import { ChangeDetectionStrategy, Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { Quarter } from '../../types/model/quarter';
import { TeamService } from '../../../services/team.service';
import { Team } from '../../types/model/team';
import { QuarterService } from '../../../services/quarter.service';
import { forkJoin, Observable, of, Subject, takeUntil } from 'rxjs';
import { ObjectiveService } from '../../../services/objective.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { State } from '../../types/enums/state';
import { ObjectiveMin } from '../../types/model/objective-min';
import { Objective } from '../../types/model/objective';
import { formInputCheck, getValueFromQuery, hasFormFieldErrors } from '../../common';
import { ActivatedRoute } from '@angular/router';
import { GJ_REGEX_PATTERN } from '../../constant-library';
import { TranslateService } from '@ngx-translate/core';
import { DialogService } from '../../../services/dialog.service';
import { KeyResultDto } from '../../types/DTOs/key-result-dto';

@Component({
  selector: 'app-objective-form',
  templateUrl: './objective-form.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class ObjectiveFormComponent implements OnInit, OnDestroy {
  objectiveForm = new FormGroup({
    title: new FormControl<string>('', [Validators.required,
      Validators.minLength(2),
      Validators.maxLength(250)]),
    description: new FormControl<string>('', [Validators.maxLength(4096)]),
    quarter: new FormControl<number>(0, [Validators.required]),
    team: new FormControl<number>({
      value: 0,
      disabled: true
    }, [Validators.required]),
    relation: new FormControl<number>({
      value: 0,
      disabled: true
    }),
    keyResults: new FormArray<FormControl<boolean | null>>([]),
    createKeyResults: new FormControl<boolean>(false)
  });

  quarters$: Observable<Quarter[]> = of([]);

  currentQuarter$: Observable<Quarter> = of();

  quarters: Quarter[] = [];

  teams$: Observable<Team[]> = of([]);

  currentTeam: Subject<Team> = new Subject<Team>();

  state: string | null = null;

  keyResults$: Observable<KeyResultDto[]> = of([]);

  keyResults: KeyResultDto[] = [];

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
    private dialogService: DialogService,
    @Inject(MAT_DIALOG_DATA)
    public data: {
      action: string;
      objective: {
        objectiveId?: number;
        teamId?: number;
      };
    },
    private translate: TranslateService
  ) {
  }

  onSubmit(submitType: any): void {
    const value = this.objectiveForm.getRawValue();
    const state = this.data.objective.objectiveId == null ? submitType : this.state;
    const objectiveDTO: Objective = {
      id: this.data.objective.objectiveId,
      version: this.version,
      quarterId: value.quarter,
      description: value.description,
      title: value.title,
      teamId: value.team,
      state: state
    } as unknown as Objective;

    const submitFunction = this.getSubmitFunction(this.data.objective.objectiveId, objectiveDTO);
    submitFunction.subscribe((savedObjective: Objective) => {
      this.closeDialog(savedObjective, false, value.createKeyResults ?? undefined);
    });
  }

  ngOnInit(): void {
    this.teams$ = this.teamService.getAllTeams()
      .pipe(takeUntil(this.unsubscribe$));
    this.quarters$ = this.quarterService.getAllQuarters();
    this.currentQuarter$ = this.quarterService.getCurrentQuarter();
    this.keyResults$ = this.data.objective.objectiveId
      ? this.objectiveService.getAllKeyResultsByObjective(this.data.objective.objectiveId || -1)
      : of([]);
    const objective$ = this.data.objective.objectiveId
      ? this.objectiveService.getFullObjective(this.data.objective.objectiveId)
      : of(this.getDefaultObjective());

    forkJoin([
      objective$,
      this.quarters$,
      this.currentQuarter$,
      this.keyResults$
    ])
      .subscribe(([
        objective,
        quarters,
        currentQuarter,
        keyResults]: [Objective, Quarter[], Quarter, KeyResultDto[]
      ]) => {
        this.handleDataInitialization(
          objective, quarters, currentQuarter, keyResults, this.data.objective.objectiveId != null
        );
      });
  }

  private handleDataInitialization(
    objective: Objective,
    quarters: Quarter[],
    currentQuarter: Quarter,
    keyResults: KeyResultDto[],
    isEditing: boolean
  ): void {
    this.quarters = quarters;

    // Determine the team ID to set in the form: existing team for editing or default team for new objectives
    const teamId = isEditing ? objective.teamId : this.data.objective.teamId;
    const newEditQuarter = isEditing ? currentQuarter.id : objective.quarterId;
    let quarterId = getValueFromQuery(this.route.snapshot.queryParams['quarter'], newEditQuarter)[0];

    if (currentQuarter && !this.isBacklogQuarter(currentQuarter.label) && this.data.action == 'releaseBacklog') {
      quarterId = quarters[1].id;
    }

    this.state = objective.state;
    this.version = objective.version;
    this.keyResults = keyResults;

    // Subscribe to teams$ to find and update the current team
    this.teams$.subscribe((teams) => {
      const currentTeam = teams.find((team) => team.id === teamId);
      if (currentTeam) {
        this.currentTeam.next(currentTeam);
      }
    });

    this.objectiveForm.patchValue({
      title: objective.title,
      description: objective.description,
      team: teamId,
      quarter: quarterId
    });

    keyResults.forEach(() => {
      this.objectiveForm.controls.keyResults.push(new FormControl<boolean>(true));
    });
  }

  ngOnDestroy() {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  getSubmitFunction(id: number | undefined, objectiveDTO: any): Observable<Objective> {
    if (this.data.action == 'duplicate' && id) {
      objectiveDTO.id = null;
      objectiveDTO.state = 'DRAFT' as State;
      return this.objectiveService.duplicateObjective(id, {
        objective: objectiveDTO,
        keyResults: this.keyResults.map((keyResult) => ({ id: keyResult.id }))
      });
    } else {
      if (this.data.action == 'releaseBacklog') {
        objectiveDTO.state = 'ONGOING' as State;
      }
      if (this.data.objective.objectiveId && id) {
        objectiveDTO.id = id;
        return this.objectiveService.updateObjective(objectiveDTO);
      }
      return this.objectiveService.createObjective(objectiveDTO);
    }
  }

  deleteObjective() {
    const dialog = this.dialogService.openConfirmDialog('CONFIRMATION.DELETE.OBJECTIVE');
    dialog.afterClosed()
      .subscribe((result) => {
        if (result && this.data.objective.objectiveId) {
          this.objectiveService.deleteObjective(this.data.objective.objectiveId)
            .subscribe({
              next: () => {
                const objectiveDTO: Objective = { id: this.data.objective.objectiveId } as unknown as Objective;
                this.closeDialog(objectiveDTO, true);
              },
              error: () => {
                this.dialogRef.close();
              }
            });
        }
      });
  }

  objectiveToObjectiveMin(objectiveDto: Objective): ObjectiveMin {
    return {
      ...objectiveDto,
      state: State[objectiveDto.state as string as keyof typeof State]
    } as unknown as ObjectiveMin;
  }

  closeDialog(objectiveDTO: Objective, willDelete = false, addKeyResult = false) {
    const value = this.objectiveForm.getRawValue();
    const objectiveMin: ObjectiveMin = this.objectiveToObjectiveMin(objectiveDTO);
    this.dialogRef.close({
      objective: objectiveMin,
      teamId: value.team,
      delete: willDelete,
      addKeyResult: addKeyResult
    });
  }

  getErrorMessage(
    error: string, field: string, firstNumber: number | null, secondNumber: number | null
  ): string {
    return field + this.translate.instant('DIALOG_ERRORS.' + error)
      .format(firstNumber, secondNumber);
  }

  getDefaultObjective() {
    return {
      id: 0,
      title: '',
      description: '',
      state: 'DRAFT' as State,
      teamId: 0,
      quarterId: 0
    } as Objective;
  }

  allowedToSaveBacklog() {
    const currentQuarter: Quarter | undefined = this.quarters.find((quarter) => quarter.id == this.objectiveForm.value.quarter);
    if (currentQuarter) {
      const isBacklogCurrent = !this.isBacklogQuarter(currentQuarter.label);
      if (this.data.action == 'duplicate') {
        return true;
      }
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

  getDialogTitle(teamName: string): string {
    if (this.data.action === 'duplicate') {
      return `Objective von ${teamName} duplizieren`;
    }

    if (this.data.action === 'releaseBacklog') {
      return 'Objective veröffentlichen';
    }

    if (!this.data.objective.objectiveId) {
      return `Objective für ${teamName} erfassen`;
    }

    if (this.data.objective.objectiveId && this.data.action !== 'releaseBacklog') {
      return `Objective von ${teamName} bearbeiten`;
    }

    return '';
  }
}
