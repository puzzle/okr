import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyResultDetailComponent } from './key-result-detail.component';
import { By } from '@angular/platform-browser';
import { MatIconModule } from '@angular/material/icon';
import { KeyResultDescriptionComponent } from '../key-result-description/key-result-description.component';
import { MatDividerModule } from '@angular/material/divider';
import { MeasureRowComponent } from '../measure-row/measure-row.component';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import {
  BrowserAnimationsModule,
  NoopAnimationsModule,
} from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatCardModule } from '@angular/material/card';
import { RouterLinkWithHref } from '@angular/router';
import { DatePipe } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';

describe('KeyResultDetailComponent', () => {
  let component: KeyResultDetailComponent;
  let fixture: ComponentFixture<KeyResultDetailComponent>;

  const mockToastrService = {
    success: jest.fn(),
    error: jest.fn(),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [
        KeyResultDetailComponent,
        KeyResultDescriptionComponent,
        MeasureRowComponent,
      ],
      providers: [
        DatePipe,
        { provide: MatDialog, useValue: {} },
        { provide: ToastrService, useValue: mockToastrService },
      ],
      imports: [
        HttpClientTestingModule,
        BrowserAnimationsModule,
        BrowserDynamicTestingModule,
        RouterTestingModule,
        MatIconModule,
        ReactiveFormsModule,
        MatInputModule,
        MatButtonModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatDividerModule,
        MatFormFieldModule,
        MatExpansionModule,
        MatCardModule,
        NoopAnimationsModule,
        RouterLinkWithHref,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(KeyResultDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    mockToastrService.success.mockReset();
    mockToastrService.error.mockReset();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have one key result description tag', () => {
    const keyResultDescription = fixture.debugElement.queryAll(
      By.css('app-key-result-description')
    );
    expect(keyResultDescription.length).toEqual(1);
  });

  it('should have two heading labels with right titles', () => {
    const headingLabel = fixture.debugElement.query(By.css('.heading-label'));
    expect(headingLabel.nativeElement.textContent).toContain(
      'Key Result Details'
    );
  });

  it('should have a title key result beschreibung', () => {
    const headingLabels = fixture.debugElement.queryAll(
      By.css('.headline-large')
    );
    expect(headingLabels.length).toEqual(1);
    expect(headingLabels[0].nativeElement.textContent).toContain(
      'Key Result Beschreibung'
    );
  });

  it('should have one mat dividers', () => {
    const dividers = fixture.debugElement.queryAll(By.css('mat-divider'));
    expect(dividers.length).toEqual(1);
  });

  it('should have one measure row tag', () => {
    const keyResultDescription = fixture.debugElement.queryAll(
      By.css('app-measure-row')
    );
    expect(keyResultDescription.length).toEqual(1);
  });
});
