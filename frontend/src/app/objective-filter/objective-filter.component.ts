import { Component } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-objective-filter',
  templateUrl: './objective-filter.component.html',
  styleUrls: ['./objective-filter.component.scss'],
})
export class ObjectiveFilterComponent {
  objectiveControl = new FormControl('', [Validators.required, Validators.email]);

  submit() {
    console.log('test');
  }
}
