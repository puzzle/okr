import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TeamComponent } from './team.component';
import { MatIcon } from '@angular/material/icon';
import { overViewEntity1 } from '../../shared/testData';
import { ObjectiveComponent } from '../objective/objective.component';
import { RouterTestingModule } from '@angular/router/testing';
import { MatMenuModule } from '@angular/material/menu';
import { KeyresultComponent } from '../keyresult/keyresult.component';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';
import { RefreshDataService } from '../../services/refresh-data.service';
import { TranslateTestingModule } from 'ngx-translate-testing';
import * as de from '../../../assets/i18n/de.json';
import { MatTooltipModule } from '@angular/material/tooltip';

const dialogMock = {
  open: jest.fn(),
};

const refreshDataServiceMock = {
  markDataRefresh: jest.fn(),
};

describe('TeamComponent', () => {
  let component: TeamComponent;
  let fixture: ComponentFixture<TeamComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        MatMenuModule,
        MatDialogModule,
        HttpClientTestingModule,
        MatTooltipModule,
        TranslateTestingModule.withTranslations({
          de: de,
        }),
      ],
      declarations: [TeamComponent, MatIcon, ObjectiveComponent, KeyresultComponent],
      providers: [
        {
          provide: MatDialog,
          useValue: dialogMock,
        },
        {
          provide: RefreshDataService,
          useValue: refreshDataServiceMock,
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(TeamComponent);
    component = fixture.componentInstance;
    component.overviewEntity = overViewEntity1;
    fixture.detectChanges();
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should display add objective button if writeable is true', async () => {
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('[data-testId="add-objective"]'));
    expect(button).toBeTruthy();
  });
});
