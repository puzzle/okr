import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ErrorComponent } from './error.component';
import { TranslateTestingModule } from 'ngx-translate-testing';
// @ts-ignore
import * as de from '../../../../assets/i18n/de.json';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

describe('ErrorComponent', () => {
  let component: ErrorComponent;
  let fixture: ComponentFixture<ErrorComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [ErrorComponent],
      providers: [TranslateService],
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
});
