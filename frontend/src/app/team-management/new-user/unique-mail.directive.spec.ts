import { UniqueEmailValidatorDirective } from './unique-mail.directive';
import { users } from '../../shared/testData';
import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { AbstractControl } from '@angular/forms';

describe('UniqueMailDirective', () => {
  const userServiceMock = {
    getUsers: jest.fn(),
  } as any;

  beforeEach(() => {
    userServiceMock.getUsers.mockReturnValue(of(users));
  });

  it('should create an instance', () => {
    TestBed.runInInjectionContext(() => {
      const directive = new UniqueEmailValidatorDirective(userServiceMock);
      expect(directive).toBeTruthy();
    });
  });

  it('should return validationError if user exists, otherwise null', () => {
    TestBed.runInInjectionContext(() => {
      const directive = new UniqueEmailValidatorDirective(userServiceMock);

      let control = { value: users[0].email } as AbstractControl;
      expect(directive.validate(control)).toStrictEqual({ notUniqueMail: { value: users[0].email } });

      control = { value: 'notexistinguser@test.com' } as AbstractControl;
      expect(directive.validate(control)).toStrictEqual(null);

      expect(directive).toBeTruthy();
    });
  });
});
