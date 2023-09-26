import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Quarter } from '../../types/model/Quarter';
import { TeamService } from '../../services/team.service';
import { Team } from '../../types/model/Team';
import { QuarterService } from '../../services/quarter.service';
import { forkJoin, Observable, of } from 'rxjs';
import { ObjectiveService } from '../../services/objective.service';
import { ObjectiveDTO } from '../../types/DTOs/ObjectiveDTO';

@Component({
  selector: 'app-objective-form',
  templateUrl: './objective-form.component.html',
  styleUrls: ['./objective-form.component.scss'],
})
export class ObjectiveFormComponent implements OnInit, OnDestroy {
  objectiveForm = new FormGroup({
    title: new FormControl('', [Validators.required, Validators.min(2), Validators.max(250)]),
    description: new FormControl('', [Validators.max(4096)]),
    quarter: new FormControl(0, [Validators.required]),
    team: new FormControl(0, [Validators.required]),
    relation: new FormControl({ value: 0, disabled: true }),
    createKeyresults: new FormControl(true),
  });

  quarters$: Observable<Quarter[]> = of([]);
  teams$: Observable<Team[]> = of([]);

  constructor(
    private teamService: TeamService,
    private quarterService: QuarterService,
    private objectiveService: ObjectiveService,
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
    console.log(objective);
    this.objectiveService.createObjective(objective).subscribe((returnValue) => console.log(returnValue));
  }

  ngOnInit(): void {
    this.teams$ = this.teamService.getAllTeams();
    this.quarters$ = this.quarterService.getAllQuarters();
    forkJoin([this.teams$, this.quarters$]).subscribe(([teams, quarters]) =>
      this.objectiveForm.patchValue({ team: teams[0].id, quarter: quarters[0].id }),
    );
  }

  ngOnDestroy(): void {
    // this.teams$.unsubscribe();
    // this.quarters$.unsubscribe();
  }
}
