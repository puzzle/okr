import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TeamComponent } from './team.component';
import { MatIcon } from '@angular/material/icon';
import { overViewEntity1 } from '../shared/testData';
import { ObjectiveComponent } from '../objective/objective.component';
import { RouterTestingModule } from '@angular/router/testing';
import { MatMenuModule } from '@angular/material/menu';
import { KeyresultComponent } from '../keyresult/keyresult.component';
import { MatDialogModule } from '@angular/material/dialog';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('TeamComponent', () => {
  let component: TeamComponent;
  let fixture: ComponentFixture<TeamComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RouterTestingModule, MatMenuModule, MatDialogModule, HttpClientTestingModule],
      declarations: [TeamComponent, MatIcon, ObjectiveComponent, KeyresultComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TeamComponent);
    component = fixture.componentInstance;
    component.overviewEntity = overViewEntity1;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
