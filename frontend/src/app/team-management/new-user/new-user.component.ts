import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
  ViewChild,
} from '@angular/core';
import { NewUser } from '../../shared/types/model/NewUser';
import { ControlContainer, NgForm, NgModel } from '@angular/forms';

@Component({
  selector: 'app-new-user',
  templateUrl: './new-user.component.html',
  styleUrl: './new-user.component.scss',
  viewProviders: [{ provide: ControlContainer, useExisting: NgForm }],
  changeDetection: ChangeDetectionStrategy.Default,
})
export class NewUserComponent implements AfterViewInit {
  randNr = Math.round(Math.random() * 10000);

  @Input({ required: true })
  index!: number;

  @Input({ required: true })
  user!: NewUser;

  @Output()
  removeUser: EventEmitter<void> = new EventEmitter<void>();

  @ViewChild('firstInput') firstInput: any;

  ngAfterViewInit(): void {
    this.firstInput.nativeElement.focus();
  }

  showError(firstName: NgModel) {
    return firstName.invalid && (firstName.dirty || firstName.touched);
  }

  remove() {
    this.removeUser.emit();
  }
}
