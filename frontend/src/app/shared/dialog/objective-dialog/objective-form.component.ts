import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Quarter } from '../../types/model/Quarter';
import { TeamService } from '../../services/team.service';
import { Team } from '../../types/model/Team';
import { QuarterService } from '../../services/quarter.service';
import { forkJoin, Observable, of } from 'rxjs';
import { ObjectiveService } from '../../services/objective.service';
import { ObjectiveDTO } from '../../types/DTOs/ObjectiveDTO';
import errorMessages from 'src/assets/errors/error-messages.json';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { State } from '../../types/enums/State';
import { ObjectiveMin } from '../../types/model/ObjectiveMin';
import { Objective } from '../../types/model/Objective';
import { ToasterService } from '../../services/toaster.service';

@Component({
  selector: 'app-objective-form',
  templateUrl: './objective-form.component.html',
  styleUrls: ['./objective-form.component.scss'],
})
export class ObjectiveFormComponent implements OnInit {
  objectiveForm = new FormGroup({
    title: new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(250)]),
    description: new FormControl('', [Validators.maxLength(4096)]),
    quarter: new FormControl(0, [Validators.required]),
    team: new FormControl(0, [Validators.required]),
    relation: new FormControl({ value: 0, disabled: true }),
    state: new FormControl(''),
    createKeyresults: new FormControl(true),
  });
  quarters$: Observable<Quarter[]> = of([]);
  teams$: Observable<Team[]> = of([]);
  protected readonly errorMessages: any = errorMessages;

  constructor(
    private teamService: TeamService,
    private quarterService: QuarterService,
    private objectiveService: ObjectiveService,
    private toasterService: ToasterService,
    public dialogRef: MatDialogRef<ObjectiveFormComponent>,
    @Inject(MAT_DIALOG_DATA)
    public data: {
      objectiveId?: number;
      teamId?: number;
    },
  ) {}

  onSubmit(event: any): void {
    const value = this.objectiveForm.value;
    const state = event.submitter.getAttribute('submitType');
    let objectiveDTO: ObjectiveDTO = {
      id: this.data.objectiveId,
      quarterId: value.quarter,
      description: value.description,
      title: value.title,
      teamId: value.team,
      state: this.data.objectiveId ? value.state : state,
    } as unknown as ObjectiveDTO;

    const submitFunction = objectiveDTO.id
      ? this.objectiveService.updateObjective(objectiveDTO)
      : this.objectiveService.createObjective(objectiveDTO);

    submitFunction.subscribe((savedObjective: ObjectiveDTO) => this.closeDialog(savedObjective));
  }

  ngOnInit(): void {
    this.teams$ = this.teamService.getAllTeams();
    this.quarters$ = this.quarterService.getAllQuarters();
    const objective$ = this.data.objectiveId
      ? this.objectiveService.getFullObjective(this.data.objectiveId)
      : of(this.getDefaultObjective());

    forkJoin([objective$, this.quarters$]).subscribe(([objective, quarters]) => {
      const teamId = this.data.objectiveId ? objective.teamId : this.data.teamId;
      const quarterId = this.data.objectiveId ? objective.quarterId : quarters[0].id;
      this.objectiveForm.patchValue({
        title: objective.title,
        description: objective.description,
        team: teamId,
        quarter: quarterId,
        state: objective.state,
      });
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

  deleteObjective() {
    this.objectiveService.deleteObjective(this.data.objectiveId!).subscribe({
      next: () => {
        let objectiveDTO: ObjectiveDTO = { id: this.data.objectiveId! } as unknown as ObjectiveDTO;
        this.closeDialog(objectiveDTO, true);
      },
      error: () => {
        this.dialogRef.close();
      },
    });
  }

  objectiveDtoToObjectiveMin(objectiveDto: ObjectiveDTO): ObjectiveMin {
    return {
      ...objectiveDto,
      state: State[objectiveDto.state],
    } as unknown as ObjectiveMin;
  }

  closeDialog(objectiveDTO: ObjectiveDTO, willDelete: boolean = false) {
    const value = this.objectiveForm.value;
    const objectiveMin: ObjectiveMin = this.objectiveDtoToObjectiveMin(objectiveDTO);
    this.dialogRef.close({ objective: objectiveMin, teamId: value.team, delete: willDelete });
  }
}
