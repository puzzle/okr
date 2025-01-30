import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageUnitsDialogComponent } from './manage-units-dialog.component';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { UnitTransformationPipe } from '../../shared/pipes/unit-transformation/unit-transformation.pipe';
import { DialogTemplateCoreComponent } from '../../shared/custom/dialog-template-core/dialog-template-core.component';
import { ErrorComponent } from '../../shared/custom/error/error.component';
import { ActionPlanComponent } from '../action-plan/action-plan.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';

describe('ManageUnitsDialogComponent', () => {
  let component: ManageUnitsDialogComponent;
  let fixture: ComponentFixture<ManageUnitsDialogComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [
        ManageUnitsDialogComponent,
        DialogTemplateCoreComponent,
        ErrorComponent,
        ActionPlanComponent
      ],
      imports: [
        TranslateModule.forRoot(),
        MatDialogModule,
        ReactiveFormsModule,
        MatIconModule,
        MatDividerModule
      ],
      providers: [
        UnitTransformationPipe,
        TranslateService,
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: MatDialogRef,
          useValue: {}
        }
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
