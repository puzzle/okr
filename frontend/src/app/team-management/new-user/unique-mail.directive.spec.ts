import { UniqueEmailValidator } from "./unique-mail.validator";
import { users } from "../../shared/testData";
import { TestBed } from "@angular/core/testing";
import { of } from "rxjs";
import { AbstractControl } from "@angular/forms";

describe("UniqueMailDirective",
  () => {
    const userServiceMock = {
      getUsers: jest.fn()
    } as any;

    beforeEach(() => {
      userServiceMock.getUsers.mockReturnValue(of(users));
    });

    it("should create an instance",
      () => {
        TestBed.runInInjectionContext(() => {
          const directive = new UniqueEmailValidator(userServiceMock);
          expect(directive)
            .toBeTruthy();
        });
      });

    it("should return validationError if user exists, otherwise null",
      () => {
        TestBed.runInInjectionContext(() => {
          const directive = new UniqueEmailValidator(userServiceMock);

          let control = { value: users[0].email } as AbstractControl;
          expect(directive.validate(control))
            .toStrictEqual({ notUniqueMail: { value: users[0].email } });

          const notExistingMail = "notexistinguser@test.com";
          control = { value: notExistingMail } as AbstractControl;
          expect(directive.validate(control))
            .toStrictEqual(null);

          directive.setAddedMails([notExistingMail]);
          expect(directive.validate(control))
            .toStrictEqual({ notUniqueMail: { value: notExistingMail } });

          expect(directive)
            .toBeTruthy();
        });
      });
  });
