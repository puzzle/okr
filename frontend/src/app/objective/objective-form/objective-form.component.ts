import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import {
  Objective,
  ObjectiveService,
} from '../../shared/services/objective.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { map, Observable, of, switchMap } from 'rxjs';
import { Team, TeamService } from '../../team/team.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-objective-form',
  templateUrl: './objective-form.component.html',
  styleUrls: ['./objective-form.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveFormComponent implements OnInit {
  objectives$!: Observable<Objective>;

  objectiveForm = new FormGroup({
    team: new FormControl<number>(0, [Validators.required]),
    title: new FormControl<string>('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(20),
    ]),
    description: new FormControl<string>('', [Validators.maxLength(500)]),
    owner: new FormControl<number>(0, [Validators.required]),
    quarter: new FormControl<number>(0),
  });
  // public users$!: Observable<User[]>;
  public objective: Objective = {
    id: 1,
    title: 'Objective name',
    description: 'description',
    quarterYear: 2022,
    quarterId: 1,
    quarterNumber: 2,
    progress: 22,
    ownerId: 21,
    ownerFirstname: 'Vorname',
    ownerLastname: 'Nachname',
    created: '2022',
  };

  public create!: boolean;

  constructor(
    // private userService: UserService,
    private objectiveService: ObjectiveService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    // this.users$ = this.userService.getUsers();
    // this.objectives$ = this.route.paramMap.pipe(
    //   switchMap(params => {
    //     const objectiveId = getNumberOrNull(params.get('id'));
    //     if (objectiveId) {
    //       this.create = false
    //       return this.objectiveService.getObjectiveById(objectiveId);
    //     } else {
    //       this.create = true
    //       return of<Objectives>(this.objectiveService.getInitKeyResult());
    //     }
    //   })
    // );
  }

  // save() {
  //   this.objectives$.pipe(
  //     map(objective => {
  //       return {
  //         ...objective,
  //         ...this.objectiveForm.value,
  //       } as Objectives;
  //     })
  //   ).subscribe(objective =>
  //     this.objectiveService.saveObjective(objective).subscribe({
  //       next: () => this.router.navigate(['/dashboard']),
  //       error: () => {
  //         console.log('Can not save this keyresult: ', objective);
  //         window.alert("Can not save this keyresult: ")
  //         return new Error('ups sommething happend');
  //       }
  //     })
  //   );
  // }
}
