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
    createKeyresults: new FormControl(true),
  });
  quarters$: Observable<Quarter[]> = of([]);
  teams$: Observable<Team[]> = of([]);
  protected readonly errorMessages: any = errorMessages;

  constructor(
    private teamService: TeamService,
    private quarterService: QuarterService,
    private objectiveService: ObjectiveService,
    public dialogRef: MatDialogRef<ObjectiveFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { objectiveId?: number; teamId?: number },
  ) {}

  onSubmit(event: any): void {
    const value = this.objectiveForm.value;
    const state = event.submitter.getAttribute('submitType');
    let objective: ObjectiveDTO = {
      quarterId: value.quarter,
      description: value.description,
      title: value.title,
      teamId: value.team,
      state: state,
    } as unknown as ObjectiveDTO;
    this.objectiveService.createObjective(objective).subscribe((returnValue) => console.log(returnValue));
  }

  ngOnInit(): void {
    this.teams$ = this.teamService.getAllTeams();
    this.quarters$ = this.quarterService.getAllQuarters();
    if (this.data.objectiveId) {
      this.objectiveService.getFullObjective(this.data.objectiveId).subscribe((objective) => {
        this.objectiveForm.patchValue({
          title: objective.title,
          description: objective.description,
          team: objective.teamId,
          quarter: objective.quarterId,
        });
      });
    } else {
      this.quarters$.subscribe((quarters) => {
        this.objectiveForm.patchValue({
          quarter: quarters[0].id,
          team: this.data.teamId,
        });
      });
    }
  }

  isTouchedOrDirty(name: string) {
    return this.objectiveForm.get(name)?.dirty || this.objectiveForm.get(name)?.touched;
  }

  getErrorKeysOfFormField(name: string) {
    const errors = this.objectiveForm.get(name)?.errors;
    return errors == null ? [] : Object.keys(errors);
  }
}
