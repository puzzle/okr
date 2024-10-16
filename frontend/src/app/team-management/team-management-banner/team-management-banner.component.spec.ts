import { TeamManagementBannerComponent } from './team-management-banner.component';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { of } from 'rxjs';
import { AddEditTeamDialog } from '../add-edit-team-dialog/add-edit-team-dialog.component';
import { OKR_DIALOG_CONFIG } from '../../shared/constantLibary';
import { SearchTeamManagementComponent } from '../search-team-management/search-team-management.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TranslateModule } from '@ngx-translate/core';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { ActivatedRoute } from '@angular/router';

describe('TeamManagementBannerComponent', () => {
  let component: TeamManagementBannerComponent;
  let fixture: ComponentFixture<TeamManagementBannerComponent>;

  const matDialogMock = {
    open: jest.fn(),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        MatDialogModule,
        MatFormFieldModule,
        MatIconModule,
        HttpClientTestingModule,
        TranslateModule.forRoot(),
        MatAutocompleteModule,
      ],
      declarations: [TeamManagementBannerComponent, SearchTeamManagementComponent],
      providers: [
        { provide: MatDialog, useValue: matDialogMock },
        { provide: ActivatedRoute, useValue: {} },
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TeamManagementBannerComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('createTeam should open dialog', fakeAsync(() => {
    matDialogMock.open.mockReturnValue({
      afterClosed: () => of(),
    });
    component.createTeam();
    tick();
    expect(matDialogMock.open).toBeCalledTimes(1);
    expect(matDialogMock.open).toBeCalledWith(AddEditTeamDialog, OKR_DIALOG_CONFIG);
  }));
});
