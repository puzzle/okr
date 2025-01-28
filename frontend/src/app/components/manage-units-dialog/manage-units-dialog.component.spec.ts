import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageUnitsDialogComponent } from './manage-units-dialog.component';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

describe('ManageUnitsDialogComponent', () => {
  let component: ManageUnitsDialogComponent;
  let fixture: ComponentFixture<ManageUnitsDialogComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [ManageUnitsDialogComponent],
      imports: [TranslateModule.forRoot()],
      providers: [
        TranslateService,
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ManageUnitsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
