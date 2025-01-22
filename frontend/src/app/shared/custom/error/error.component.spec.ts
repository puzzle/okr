import { TestBed } from '@angular/core/testing';

import { ErrorComponent } from './error.component';
import { TranslateService } from '@ngx-translate/core';
import { FormControl, FormGroup, FormGroupDirective, Validators } from '@angular/forms';

describe('ErrorComponent', () => {
  let component: ErrorComponent;
  let translateService: TranslateService;
  let formGroupDirective: FormGroupDirective;

  beforeEach(() => {
    const translateServiceMock = {
      instant: jest.fn()
    };

    TestBed.configureTestingModule({
      providers: [{ provide: TranslateService,
        useValue: translateServiceMock },
      FormGroupDirective]
    });

    translateService = TestBed.inject(TranslateService);
    formGroupDirective = TestBed.inject(FormGroupDirective);
    translateServiceMock.instant.mockReturnValue('Translated error');
    component = new ErrorComponent(translateService, formGroupDirective);
  });

  it('should return no error messages if form field is clean or untouched or has no errors', () => {
    component.form = new FormGroup({
      testField: new FormControl('', Validators.required)
    });
    component.controlPath = ['testField'];

    const errorMessages = component.getErrorMessages();

    expect(errorMessages)
      .toEqual([]);
  });

  it('should return error messages if form field is dirty and touched with errors', () => {
    const formControl = new FormControl('', [Validators.required]);
    formControl.markAsDirty();
    formControl.markAsTouched();
    formControl.setErrors({ required: true });

    component.form = new FormGroup({ testField: formControl });
    component.controlPath = ['testField'];
    component.name = 'Test Field';

    const errorMessages = component.getErrorMessages();

    expect(errorMessages)
      .toEqual(['Translated error']);
  });

  it('should build an error message with a translated key and parameters', () => {
    const errorMessage = component.buildErrorMessage('required', { min: 5 });

    expect(errorMessage)
      .toBe('Translated error');
  });

  it('should traverse the control path to get the correct form control', () => {
    const nestedFormGroup = new FormGroup({
      nestedField: new FormControl('', Validators.required)
    });
    const rootFormGroup = new FormGroup({
      parentField: nestedFormGroup
    });

    component.form = rootFormGroup;
    component.controlPath = ['parentField',
      'nestedField'];

    const formControl = component.getFormControl();

    expect(formControl)
      .toBe(nestedFormGroup.get('nestedField'));
  });
});
