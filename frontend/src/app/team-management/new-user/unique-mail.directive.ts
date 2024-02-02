import { Directive } from '@angular/core';
import { AbstractControl, NG_VALIDATORS, ValidationErrors, Validator } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Directive({
  selector: '[appUniqueEmail]',
  providers: [
    {
      provide: NG_VALIDATORS,
      useExisting: UniqueEmailValidatorDirective,
      multi: true,
    },
  ],
})
export class UniqueEmailValidatorDirective implements Validator {
  private existingUserMails: string[] = [];

  constructor(private readonly userService: UserService) {
    this.userService
      .getUsers()
      .pipe(takeUntilDestroyed())
      .subscribe((users) => {
        this.existingUserMails = users.map((u) => u.email);
      });
  }

  validate(control: AbstractControl): ValidationErrors | null {
    const existingUser = this.existingUserMails.includes(control.value);
    return existingUser ? { notUniqueMail: { value: control.value } } : null;
  }
}
