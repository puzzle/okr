import { Injectable, inject } from '@angular/core';
import { AbstractControl, ValidationErrors, Validator } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Injectable({ providedIn: 'root' })
export class UniqueEmailValidator implements Validator {
  private readonly userService = inject(UserService);

  private existingUserMails: string[] = []; // mails exsiting already in backend

  private addedMails: string[] = []; // mails added in form

  constructor() {
    this.userService
      .getUsers()
      .pipe(takeUntilDestroyed())
      .subscribe((users) => {
        this.existingUserMails = users.map((u) => u.email);
      });
  }

  validate(control: AbstractControl): ValidationErrors | null {
    const existingUser = this.existingUserMails.concat(this.addedMails)
      .includes(control.value);
    return existingUser ? { notUniqueMail: { value: control.value } } : null;
  }

  setAddedMails(mails: string[]) {
    this.addedMails = mails;
  }
}
