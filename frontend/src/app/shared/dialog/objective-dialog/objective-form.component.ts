import { ChangeDetectionStrategy, Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Quarter } from '../../types/model/Quarter';
import { TeamService } from '../../services/team.service';
import { Team } from '../../types/model/Team';
import { QuarterService } from '../../services/quarter.service';
import { forkJoin, Observable, of } from 'rxjs';
import { ObjectiveService } from '../../services/objective.service';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { State } from '../../types/enums/State';
import { ObjectiveMin } from '../../types/model/ObjectiveMin';
import { Objective } from '../../types/model/Objective';
import errorMessages from '../../../../assets/errors/error-messages.json';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-objective-form',
  templateUrl: './objective-form.component.html',
  styleUrls: ['./objective-form.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveFormComponent implements OnInit {
  objectiveForm = new FormGroup({
    title: new FormControl<string>('', [Validators.required, Validators.minLength(2), Validators.maxLength(250)]),
    description: new FormControl<string>('', [Validators.maxLength(4096)]),
    quarter: new FormControl<number>(0, [Validators.required]),
    team: new FormControl<number>({ value: 0, disabled: true }, [Validators.required]),
    relation: new FormControl<number>({ value: 0, disabled: true }),
    createKeyResults: new FormControl<boolean>(false),
  });
  quarters$: Observable<Quarter[]> = of([]);
  teams$: Observable<Team[]> = of([]);
  currentTeam: Team = {} as Team;
  protected readonly errorMessages: any = errorMessages;

  constructor(
    private teamService: TeamService,
    private quarterService: QuarterService,
    private objectiveService: ObjectiveService,
    public dialogRef: MatDialogRef<ObjectiveFormComponent>,
    private dialog: MatDialog,
    @Inject(MAT_DIALOG_DATA)
    public data: {
      objectiveId?: number;
      teamId?: number;
    },
  ) {}

  onSubmit(event: any): void {
    const value = this.objectiveForm.getRawValue();
    const state = event.submitter.getAttribute('submitType');
    let objectiveDTO: Objective = {
      id: this.data.objectiveId,
      quarterId: value.quarter,
      description: value.description,
      title: value.title,
      teamId: value.team,
      state: state,
    } as unknown as Objective;

    const submitFunction = objectiveDTO.id
      ? this.objectiveService.updateObjective(objectiveDTO)
      : this.objectiveService.createObjective(objectiveDTO);

    submitFunction.subscribe((savedObjective: Objective) =>
      this.closeDialog(savedObjective, false, value.createKeyResults!),
    );
  }

  ngOnInit(): void {
    this.teams$ = this.teamService.getAllTeams();
    this.quarters$ = this.quarterService.getAllQuarters();
    const objective$ = this.data.objectiveId
      ? this.objectiveService.getFullObjective(this.data.objectiveId)
      : of(this.getDefaultObjective());

    forkJoin([objective$, this.quarters$]).subscribe(([objective, quarters]) => {
      const teamId = this.data.objectiveId ? objective.teamId : this.data.teamId;
      this.teams$.subscribe((value) => {
        this.currentTeam = value.filter((team) => team.id == teamId)[0];
      });
      const quarterId = this.data.objectiveId ? objective.quarterId : quarters[0].id;
      this.objectiveForm.patchValue({
        title: objective.title,
        description: objective.description,
        team: teamId,
        quarter: quarterId,
      });
    });
  }

  deleteObjective() {
    const dialog = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Objective',
      },
    });
    dialog.afterClosed().subscribe((result) => {
      if (result) {
        this.objectiveService.deleteObjective(this.data.objectiveId!).subscribe({
          next: () => {
            let objectiveDTO: Objective = { id: this.data.objectiveId! } as unknown as Objective;
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

  isTouchedOrDirty(name: string) {
    return this.objectiveForm.get(name)?.dirty || this.objectiveForm.get(name)?.touched;
  }
  getErrorKeysOfFormField(name: string) {
    const errors = this.objectiveForm.get(name)?.errors;
    return errors == null ? [] : Object.keys(errors);
  }

  getDefaultObjective() {
    return {
      id: 0,
      title: '',
      description: '',
      state: State.DRAFT,
      teamId: 0,
      quarterId: 0,
    } as Objective;
  }
}
