import { TeamManagementComponent } from './team-management.component';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HarnessLoader } from '@angular/cdk/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { RouterTestingModule } from '@angular/router/testing';
import { DialogHeaderComponent } from '../../custom/dialog-header/dialog-header.component';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';

const dialogMock = {
  close: jest.fn(),
};

describe('TeamManagementComponent', () => {
  let component: TeamManagementComponent;
  let fixture: ComponentFixture<TeamManagementComponent>;
  let loader: HarnessLoader;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        MatDialogModule,
        MatIconModule,
        MatFormFieldModule,
        MatSelectModule,
        ReactiveFormsModule,
        MatInputModule,
        NoopAnimationsModule,
        MatCheckboxModule,
        RouterTestingModule,
      ],
      declarations: [TeamManagementComponent, DialogHeaderComponent],
      providers: [
        {
          provide: MatDialogRef,
          useValue: dialogMock,
        },
        {
          provide: MAT_DIALOG_DATA,
          useValue: null,
        },
      ],
    });
    fixture = TestBed.createComponent(TeamManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
