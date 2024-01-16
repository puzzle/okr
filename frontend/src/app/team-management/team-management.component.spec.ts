import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TeamManagementComponent } from './team-management.component';
import { TeamManagementBannerComponent } from './team-management-banner/team-management-banner.component';
import { TeamListComponent } from './team-list/team-list.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { MemberListComponent } from './member-list/member-list.component';
import { SearchTeamManagementComponent } from './search-team-management/search-team-management.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { of } from 'rxjs';
import { SharedModule } from '../shared/shared.module';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatListModule } from '@angular/material/list';
import { TranslateModule } from '@ngx-translate/core';
import { MatAutocompleteModule } from '@angular/material/autocomplete';

describe('TeamManagementComponent', () => {
  let component: TeamManagementComponent;
  let fixture: ComponentFixture<TeamManagementComponent>;

  const activatedRouteMock = {
    paramMap: of({}),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        MatFormFieldModule,
        MatIconModule,
        SharedModule,
        MatInputModule,
        BrowserAnimationsModule,
        MatListModule,
        MatAutocompleteModule,
        TranslateModule.forRoot(),
      ],
      declarations: [
        TeamManagementComponent,
        TeamManagementBannerComponent,
        TeamListComponent,
        MemberListComponent,
        SearchTeamManagementComponent,
      ],
      providers: [{ provide: ActivatedRoute, useValue: activatedRouteMock }],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TeamManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
