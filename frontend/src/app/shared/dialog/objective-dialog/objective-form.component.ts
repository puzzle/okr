import { AfterViewInit, Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Quarter } from '../../types/model/Quarter';
import { TeamService } from '../../services/team.service';
import { Team } from '../../types/model/Team';
import { QuarterService } from '../../services/quarter.service';
import { forkJoin, Observable, of } from 'rxjs';
import { Objective } from '../../types/model/Objective';

@Component({
  selector: 'app-objective-form',
  templateUrl: './objective-form.component.html',
  styleUrls: ['./objective-form.component.scss'],
})
export class ObjectiveFormComponent implements OnInit {
  objectiveForm = new FormGroup({
    title: new FormControl(''),
    description: new FormControl(''),
    quarter: new FormControl(),
    team: new FormControl(),
    relation: new FormControl(''),
    createKeyresults: new FormControl(true),
  });

  quarters$: Observable<Quarter[]> = of([]);
  teams$: Observable<Team[]> = of([]);

  constructor(
    private teamService: TeamService,
    private quarterService: QuarterService,
  ) {}

  onSubmit(event: any): void {
    console.log(this.objectiveForm.value);
    const objective: Objective = this.objectiveForm.value as Objective;
    const type = event.submitter.getAttribute('submitType');
    if (type == 'submit') {
      console.log('submit');
    } else if (type == 'draft') {
      console.log('draft');
    }
  }

  ngOnInit(): void {
    this.teams$ = this.teamService.getAllTeams();
    this.quarters$ = this.quarterService.getAllQuarters();
    forkJoin([this.teams$, this.quarters$]).subscribe(([teams, quarters]) =>
      this.objectiveForm.patchValue({ team: teams[0].id, quarter: quarters[0].id }),
    );
  }
}
