import { ChangeDetectionStrategy, Component, ElementRef, Inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Quarter } from '../../types/model/Quarter';
import { TeamService } from '../../../services/team.service';
import { Team } from '../../types/model/Team';
import { QuarterService } from '../../../services/quarter.service';
import { forkJoin, Observable, of, Subject, takeUntil } from 'rxjs';
import { ObjectiveService } from '../../../services/objective.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { State } from '../../types/enums/State';
import { ObjectiveMin } from '../../types/model/ObjectiveMin';
import { Objective } from '../../types/model/Objective';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { formInputCheck, getValueFromQuery, hasFormFieldErrors, isMobileDevice } from '../../common';
import { ActivatedRoute } from '@angular/router';
import { GJ_REGEX_PATTERN } from '../../constantLibary';
import { TranslateService } from '@ngx-translate/core';
import { DialogService } from '../../../services/dialog.service';
import { AlignmentPossibility } from '../../types/model/AlignmentPossibility';
import { AlignmentPossibilityObject } from '../../types/model/AlignmentPossibilityObject';

@Component({
  selector: 'app-objective-form',
  templateUrl: './objective-form.component.html',
  styleUrls: ['./objective-form.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveFormComponent implements OnInit, OnDestroy {
  @ViewChild('input') input!: ElementRef<HTMLInputElement>;
  filteredOptions$: BehaviorSubject<AlignmentPossibility[]> = new BehaviorSubject<AlignmentPossibility[]>([]);
  objectiveForm = new FormGroup({
    title: new FormControl<string>('', [Validators.required, Validators.minLength(2), Validators.maxLength(250)]),
    description: new FormControl<string>('', [Validators.maxLength(4096)]),
    quarter: new FormControl<number>(0, [Validators.required]),
    team: new FormControl<number>({ value: 0, disabled: true }, [Validators.required]),
    alignment: new FormControl<AlignmentPossibilityObject | null>(null),
    createKeyResults: new FormControl<boolean>(false),
  });
  quarters$: Observable<Quarter[]> = of([]);
  currentQuarter$: Observable<Quarter> = of();
  quarters: Quarter[] = [];
  objective: Objective | null = null;
  teams$: Observable<Team[]> = of([]);
  alignmentPossibilities$: Observable<AlignmentPossibility[]> = of([]);
  currentTeam$: BehaviorSubject<Team | null> = new BehaviorSubject<Team | null>(null);
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
    private dialogService: DialogService,
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

    let alignmentEntity: AlignmentPossibilityObject | null = value.alignment;
    let alignment: string | null = alignmentEntity
      ? (alignmentEntity.objectType == 'objective' ? 'O' : 'K') + alignmentEntity.objectId
      : null;

    let objectiveDTO: Objective = {
      id: this.data.objective.objectiveId,
      version: this.version,
      quarterId: value.quarter,
      description: value.description,
      title: value.title,
      teamId: value.team,
      state: state,
      alignedEntityId: alignment,
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
    this.currentQuarter$ = this.quarterService.getCurrentQuarter();
    const objective$ = isCreating
      ? this.objectiveService.getFullObjective(this.data.objective.objectiveId!)
      : of(this.getDefaultObjective());
    forkJoin([objective$, this.quarters$, this.currentQuarter$]).subscribe(([objective, quarters, currentQuarter]) => {
      this.quarters = quarters;
      this.objective = objective;
      const teamId = isCreating ? objective.teamId : this.data.objective.teamId;
      const newEditQuarter = isCreating ? currentQuarter.id : objective.quarterId;
      let quarterId = getValueFromQuery(this.route.snapshot.queryParams['quarter'], newEditQuarter)[0];

      if (currentQuarter && !this.isBacklogQuarter(currentQuarter.label) && this.data.action == 'releaseBacklog') {
        quarterId = quarters[2].id;
      }

      this.state = objective.state;
      this.version = objective.version;
      this.teams$.subscribe((value) => {
        let team: Team = value.filter((team: Team) => team.id == teamId)[0];
        this.currentTeam$.next(team);
      });
      this.generateAlignmentPossibilities(quarterId, objective, teamId!);

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
    const dialog = this.dialogService.openConfirmDialog('CONFIRMATION.DELETE.OBJECTIVE');
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
      alignedEntityId: null,
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
  generateAlignmentPossibilities(quarterId: number, objective: Objective | null, teamId: number | null) {
    this.alignmentPossibilities$ = this.objectiveService.getAlignmentPossibilities(quarterId);
    this.alignmentPossibilities$.subscribe((value: AlignmentPossibility[]) => {
      if (teamId) {
        value = value.filter((item: AlignmentPossibility) => !(item.teamId == teamId));
      }

      if (objective) {
        let alignment: string | null = objective.alignedEntityId;
        if (alignment) {
          let alignmentType: string = alignment.charAt(0);
          let alignmentId: number = parseInt(alignment.substring(1));
          alignmentType = alignmentType == 'O' ? 'objective' : 'keyResult';
          let element: AlignmentPossibilityObject | null = this.findAlignmentObject(value, alignmentId, alignmentType);
          this.objectiveForm.patchValue({
            alignment: element,
          });
        }
      }

      this.filteredOptions$.next(value.slice());
      this.alignmentPossibilities$ = of(value);
    });
  }

  findAlignmentObject(
    alignmentPossibilities: AlignmentPossibility[],
    objectId: number,
    objectType: string,
  ): AlignmentPossibilityObject | null {
    for (let possibility of alignmentPossibilities) {
      let foundObject: AlignmentPossibilityObject | undefined = possibility.alignmentObjectDtos.find(
        (alignmentObject: AlignmentPossibilityObject) =>
          alignmentObject.objectId === objectId && alignmentObject.objectType === objectType,
      );
      if (foundObject) {
        return foundObject;
      }
    }
    return null;
  }

  updateAlignments() {
    this.input.nativeElement.value = '';
    this.filteredOptions$.next([]);
    this.objectiveForm.patchValue({
      alignment: null,
    });
    this.generateAlignmentPossibilities(this.objectiveForm.value.quarter!, null, this.currentTeam$.getValue()!.id);
  }

  filter() {
    let filterValue = this.input.nativeElement.value.toLowerCase();
    this.alignmentPossibilities$.subscribe((alignmentPossibilities: AlignmentPossibility[]) => {
      let filteredObjects: AlignmentPossibilityObject[] = alignmentPossibilities.flatMap(
        (alignmentPossibility: AlignmentPossibility) =>
          alignmentPossibility.alignmentObjectDtos.filter((alignmentPossibilityObject: AlignmentPossibilityObject) =>
            alignmentPossibilityObject.objectTitle.toLowerCase().includes(filterValue),
          ),
      );

      let matchingPossibilities: AlignmentPossibility[] = alignmentPossibilities.filter(
        (possibility: AlignmentPossibility) =>
          filteredObjects.some((alignmentPossibilityObject: AlignmentPossibilityObject) =>
            possibility.alignmentObjectDtos.includes(alignmentPossibilityObject),
          ),
      );

      matchingPossibilities = [...new Set(matchingPossibilities)];

      let optionList = matchingPossibilities.map((possibility: AlignmentPossibility) => ({
        ...possibility,
        alignmentObjectDtos: possibility.alignmentObjectDtos.filter(
          (alignmentPossibilityObject: AlignmentPossibilityObject) =>
            filteredObjects.includes(alignmentPossibilityObject),
        ),
      }));

      if (optionList.length == 0) {
        this.filteredOptions$.next(
          alignmentPossibilities.filter((possibility: AlignmentPossibility) =>
            possibility.teamName.toLowerCase().includes(filterValue),
          ),
        );
      } else {
        this.filteredOptions$.next(optionList);
      }
    });
  }

  displayWith(value: any) {
    if (value) {
      return value.objectTitle;
    }
  }

  get displayedValue(): string {
    if (this.input) {
      return this.input.nativeElement.value;
    } else {
      return '';
    }
  }

  scrollLeft() {
    this.input.nativeElement.scrollLeft = 0;
  }

  protected readonly getQuarterLabel = getQuarterLabel;
}
