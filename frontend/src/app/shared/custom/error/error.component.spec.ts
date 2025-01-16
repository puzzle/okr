import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ErrorComponent } from './error.component';
import { TranslateTestingModule } from 'ngx-translate-testing';
// @ts-ignore
import * as de from '../../../../assets/i18n/de.json';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { FormControl, FormGroup, FormGroupDirective } from '@angular/forms';

describe('ErrorComponent', () => {
  let component: ErrorComponent;
  let fixture: ComponentFixture<ErrorComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [ErrorComponent],
      providers: [TranslateService,
        FormGroupDirective,
        {
          provide: FormGroupDirective,
          useValue: new FormGroupDirective([], [])
        }],
      imports: [TranslateModule.forRoot(),
        TranslateTestingModule.withTranslations({
          de: de
        })]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ErrorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  const controlPath = ['title'];

  const form = new FormGroup({
    title: new FormControl<string>('')
  });

  const name = 'Title';


  it('should get right error message', () => {

  });
});
