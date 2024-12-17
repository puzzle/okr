import { Component } from '@angular/core';
import { NewUser } from '../../shared/types/model/NewUser';
import { FormArray, FormControl, FormGroup, NonNullableFormBuilder, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { DialogRef } from '@angular/cdk/dialog';
import { NewUserForm } from '../../shared/types/model/NewUserForm';
import { UniqueEmailValidator } from '../new-user/unique-mail.validator';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-invite-user-dialog',
  templateUrl: './invite-user-dialog.component.html',
  styleUrl: './invite-user-dialog.component.scss',
})
export class InviteUserDialogComponent {
  form: FormArray<FormGroup<NewUserForm<FormControl>>>;
  triedToSubmit = false;

  constructor(
    private readonly userService: UserService,
    private readonly dialogRef: DialogRef,
    private readonly formBuilder: NonNullableFormBuilder,
    private readonly uniqueMailValidator: UniqueEmailValidator,
  ) {
    this.form = this.formBuilder.array([this.createUserFormGroup()]);
    this.form.valueChanges
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.uniqueMailValidator.setAddedMails(this.extractAddedMails()));
  }

  registerUsers() {
    this.triedToSubmit = true;
    if (!this.form.valid) {
      return;
    }
    this.userService.createUsers(this.extractFormValue()).subscribe(() => this.dialogRef.close());
  }

  private extractFormValue(): NewUser[] {
    return this.form.value as NewUser[];
  }

  addUser() {
    this.form.push(this.createUserFormGroup());
  }

  removeUser(index: number) {
    this.form.removeAt(index);
  }

  private createUserFormGroup() {
    return this.formBuilder.group({
      firstName: this.formBuilder.control('', [Validators.required, Validators.minLength(1)]),
      lastName: this.formBuilder.control('', [Validators.required, Validators.minLength(1)]),
      email: this.formBuilder.control('', [
        Validators.required,
        Validators.minLength(1),
        Validators.email,
        this.uniqueMailValidator.validate.bind(this.uniqueMailValidator),
      ]),
    });
  }

  private extractAddedMails() {
    if (!this.form) {
      return [];
    }
    return this.extractFormValue()
      .map((u) => u.email)
      .filter((mail) => !!mail);
  }
}
