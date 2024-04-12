import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
  ViewChild,
} from '@angular/core';
import {ControlContainer, FormControl, FormGroup, NgForm} from '@angular/forms';
import {NewUserForm} from "../../shared/types/model/NewUserForm";

@Component({
  selector: 'app-new-user',
  templateUrl: './new-user.component.html',
  styleUrl: './new-user.component.scss',
  viewProviders: [{ provide: ControlContainer, useExisting: NgForm }],
  changeDetection: ChangeDetectionStrategy.Default,
})
export class NewUserComponent implements AfterViewInit {
  @Input({ required: true })
  index!: number;

  @Input({ required: true })
  userFormGroup!: FormGroup<NewUserForm<FormControl>>;

  @Input({ required: true })
  triedToSubmit!: boolean;

  @Output()
  removeUser: EventEmitter<void> = new EventEmitter<void>();

  @ViewChild('firstInput') firstInput: any;

  ngAfterViewInit(): void {
    this.firstInput.nativeElement.focus();
  }

  remove() {
    this.removeUser.emit();
  }

  get firstname() {
    return this.userFormGroup.controls.firstname;
  }

  get lastname() {
    return this.userFormGroup.controls.lastname;
  }

  get email() {
    return this.userFormGroup.controls.email;
  }
}
