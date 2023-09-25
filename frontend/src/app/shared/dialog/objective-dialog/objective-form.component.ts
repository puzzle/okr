import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Quarter } from '../../types/model/Quarter';
import { Observable } from 'rxjs';
import { TeamMin } from '../../types/model/TeamMin';

@Component({
  selector: 'app-objective-form',
  templateUrl: './objective-form.component.html',
  styleUrls: ['./objective-form.component.scss'],
})
export class ObjectiveFormComponent {
  objectiveForm = new FormGroup({
    titel: new FormControl(''),
    description: new FormControl(''),
    quartal: new FormControl(''),
    team: new FormControl(''),
    createKeyresults: new FormControl(''),
  });
  quarters: Observable<Quarter[]> | undefined;
  teams: Observable<TeamMin[]> | undefined;

  onSubmit() {}
}
