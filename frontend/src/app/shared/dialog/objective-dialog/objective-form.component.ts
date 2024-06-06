import { ChangeDetectionStrategy, Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Quarter } from '../../types/model/Quarter';
import { TeamService } from '../../services/team.service';
import { Team } from '../../types/model/Team';
import { QuarterService } from '../../services/quarter.service';
import { BehaviorSubject, forkJoin, Observable, of } from 'rxjs';
import { ObjectiveService } from '../../services/objective.service';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { State } from '../../types/enums/State';
import { ObjectiveMin } from '../../types/model/ObjectiveMin';
import { Objective } from '../../types/model/Objective';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { formInputCheck, getQuarterLabel, getValueFromQuery, hasFormFieldErrors, isMobileDevice } from '../../common';
import { ActivatedRoute } from '@angular/router';
import { CONFIRM_DIALOG_WIDTH, GJ_REGEX_PATTERN } from '../../constantLibary';
import { TranslateService } from '@ngx-translate/core';
import { AlignmentPossibility } from '../../types/model/AlignmentPossibility';
import { AlignmentPossibilityObject } from '../../types/model/AlignmentPossibilityObject';

@Component({
  selector: 'app-objective-form',
  templateUrl: './objective-form.component.html',
  styleUrls: ['./objective-form.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveFormComponent implements OnInit {
  @ViewChild('alignmentInput') alignmentInput!: ElementRef<HTMLInputElement>;

  objectiveForm = new FormGroup({
    title: new FormControl<string>('', [Validators.required, Validators.minLength(2), Validators.maxLength(250)]),
    description: new FormControl<string>('', [Validators.maxLength(4096)]),
    quarter: new FormControl<number>(0, [Validators.required]),
    team: new FormControl<number>({ value: 0, disabled: true }, [Validators.required]),
    alignment: new FormControl<AlignmentPossibilityObject | null>(null),
    createKeyResults: new FormControl<boolean>(false),
  });
  quarters$: Observable<Quarter[]> = of([]);
  quarters: Quarter[] = [];
  teams$: Observable<Team[]> = of([]);
  alignmentPossibilities$: Observable<AlignmentPossibility[]> = of([]);
  filteredAlignmentOptions$: BehaviorSubject<AlignmentPossibility[]> = new BehaviorSubject<AlignmentPossibility[]>([]);
  currentTeam$: BehaviorSubject<Team | null> = new BehaviorSubject<Team | null>(null);
  state: string | null = null;
  version!: number;
  protected readonly formInputCheck = formInputCheck;
  protected readonly hasFormFieldErrors = hasFormFieldErrors;

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
        teamVersion?: number;
      };
    },
    private translate: TranslateService,
  ) {}

  onSubmit(submitType: any): void {
    const value = this.objectiveForm.getRawValue();
    const state = this.data.objective.objectiveId == null ? submitType : this.state;

    let alignment: AlignmentPossibilityObject | null = value.alignment;
    let alignmentEntity: string | null = alignment
      ? (alignment.objectType == 'objective' ? 'O' : 'K') + alignment.objectId
      : null;

    let objectiveDTO: Objective = {
      id: this.data.objective.objectiveId,
      version: this.version,
      quarterId: value.quarter,
      description: value.description,
      title: value.title,
      teamId: value.team,
      state: state,
      alignedEntityId: alignmentEntity,
    } as unknown as Objective;

    const submitFunction = this.getSubmitFunction(objectiveDTO.id, objectiveDTO);
    submitFunction.subscribe((savedObjective: Objective) =>
      this.closeDialog(savedObjective, false, value.createKeyResults!),
    );
  }

  ngOnInit(): void {
    const isCreating: boolean = !!this.data.objective.objectiveId;
    this.teams$ = this.teamService.getAllTeams();
    this.quarters$ = this.quarterService.getAllQuarters();
    const objective$ = isCreating
      ? this.objectiveService.getFullObjective(this.data.objective.objectiveId!)
      : of(this.getDefaultObjective());

    forkJoin([objective$, this.quarters$]).subscribe(([objective, quarters]) => {
      this.quarters = quarters;
      const teamId = isCreating ? objective.teamId : this.data.objective.teamId;
      let quarterId = getValueFromQuery(this.route.snapshot.queryParams['quarter'], quarters[2].id)[0];

      let currentQuarter: Quarter | undefined = this.quarters.find((quarter) => quarter.id == quarterId);
      if (currentQuarter && !this.isNotBacklogQuarter(currentQuarter.label) && this.data.action == 'releaseBacklog') {
        quarterId = quarters[2].id;
      }

      this.state = objective.state;
      this.version = objective.version;
      this.teams$.subscribe((value) => {
        this.currentTeam$.next(value.filter((team: Team) => team.id == teamId)[0]);
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
      alignedEntityId: null,
    } as Objective;
  }

  allowedToSaveBacklog() {
    let currentQuarter: Quarter | undefined = this.quarters.find(
      (quarter) => quarter.id == this.objectiveForm.value.quarter,
    );
    if (currentQuarter) {
      let isBacklogCurrent: boolean = this.isNotBacklogQuarter(currentQuarter.label);
      if (this.data.action == 'duplicate') return true;
      if (this.data.objective.objectiveId) {
        return !isBacklogCurrent ? this.state == 'DRAFT' : true;
      } else {
        return isBacklogCurrent;
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

  isNotBacklogQuarter(label: string) {
    return GJ_REGEX_PATTERN.test(label);
  }

  generateAlignmentPossibilities(quarterId: number, objective: Objective | null, teamId: number | null) {
    this.alignmentPossibilities$ = this.objectiveService.getAlignmentPossibilities(quarterId);
    this.alignmentPossibilities$.subscribe((value: AlignmentPossibility[]) => {
      if (teamId) {
        value = value.filter((item: AlignmentPossibility) => item.teamId != teamId);
      }

      if (objective) {
        let alignmentEntity: string | null = objective.alignedEntityId;
        if (alignmentEntity) {
          let alignmentType: string = alignmentEntity.charAt(0);
          let alignmentId: number = parseInt(alignmentEntity.substring(1));
          alignmentType = alignmentType == 'O' ? 'objective' : 'keyResult';
          let alignmentPossibilityObject: AlignmentPossibilityObject | null = this.findAlignmentPossibilityObject(
            value,
            alignmentId,
            alignmentType,
          );
          this.objectiveForm.patchValue({
            alignment: alignmentPossibilityObject,
          });
        }
      }

      this.filteredAlignmentOptions$.next(value.slice());
      this.alignmentPossibilities$ = of(value);
    });
  }

  findAlignmentPossibilityObject(
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
    this.alignmentInput.nativeElement.value = '';
    this.filteredAlignmentOptions$.next([]);
    this.objectiveForm.patchValue({
      alignment: null,
    });
    this.generateAlignmentPossibilities(this.objectiveForm.value.quarter!, null, this.currentTeam$.getValue()!.id);
  }

  filter() {
    let filterValue: string = this.alignmentInput.nativeElement.value.toLowerCase();
    this.alignmentPossibilities$.subscribe((alignmentPossibilities: AlignmentPossibility[]) => {
      let matchingTeams: AlignmentPossibility[] = alignmentPossibilities.filter((possibility: AlignmentPossibility) =>
        possibility.teamName.toLowerCase().includes(filterValue),
      );

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

      let alignmentOptionList = matchingPossibilities.map((possibility: AlignmentPossibility) => ({
        ...possibility,
        alignmentObjectDtos: possibility.alignmentObjectDtos.filter(
          (alignmentPossibilityObject: AlignmentPossibilityObject) =>
            filteredObjects.includes(alignmentPossibilityObject),
        ),
      }));

      let concatAlignmentOptionList: AlignmentPossibility[] =
        filterValue == '' ? matchingTeams : matchingTeams.concat(alignmentOptionList);
      this.filteredAlignmentOptions$.next([...new Set(concatAlignmentOptionList)]);
    });
  }

  displayWith(value: any) {
    if (value) {
      return value.objectTitle;
    }
  }

  get displayedValue(): string {
    if (this.alignmentInput) {
      return this.alignmentInput.nativeElement.value;
    } else {
      return '';
    }
  }

  scrollLeft() {
    this.alignmentInput.nativeElement.scrollLeft = 0;
  }

  protected readonly getQuarterLabel = getQuarterLabel;
}
