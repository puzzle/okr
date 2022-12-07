import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { User, UserService } from './user.service';
import { Observable } from 'rxjs';
import { Objective } from '../team-detail/objective.service';
import { ActivatedRoute } from '@angular/router';
import {
  ExpectedEvolution,
  KeyResultMeasure,
  KeyResultService,
  Unit,
} from '../shared/services/key-result.service';

@Component({
  selector: 'app-key-result-form',
  templateUrl: './key-result-form.component.html',
  styleUrls: ['./key-result-form.component.scss'],
})
export class KeyResultFormComponent implements OnInit {
  keyResultForm = new FormGroup({
    title: new FormControl(''),
    unit: new FormControl(''),
    expectedEvolution: new FormControl(''),
    basicValue: new FormControl(''),
    targetValue: new FormControl(''),
    description: new FormControl(''),
    ownerId: new FormControl(''),
  });

  public users$!: Observable<User[]>;
  public Unit = Unit;
  public ExpectedEvolution = ExpectedEvolution;
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
  };
  public keyResult!: Observable<KeyResultMeasure>;
  public id!: number;

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private keyResultService: KeyResultService
  ) {}

  ngOnInit(): void {
    this.users$ = this.userService.getAllUsers();
    this.route.params.subscribe((params) => {
      this.id = params['id'];
      if (this.id) {
        this.keyResult = this.keyResultService.getKeyResultById(this.id);
      }
    });
  }

  submit() {
    let keyResult = {
      ...this.getBaseKeyResult(),
      ...this.keyResultForm.value,
      id: this.id,
      objectiveId: this.objective.id,
    } as KeyResultMeasure;
    console.log(keyResult);
    if (this.id) {
      //TODO update keyResult
      return;
    }
    //TODO create new keyResult
  }

  getBaseKeyResult() {
    return {
      id: 0,
      title: '',
      description: '',
      expectedEvolution: ExpectedEvolution.CONSTANT,
      unit: Unit.BINARY,
      ownerId: 0,
      ownerLastname: '',
      ownerFirstname: '',
      quarterId: 0,
      quarterNumber: 1,
      quarterYear: 2022,
      targetValue: 1,
      basicValue: 1,
      objectiveId: 1,
    };
  }
}
