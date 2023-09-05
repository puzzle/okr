import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamComponent } from './team.component';
import { MatIcon } from '@angular/material/icon';
import { TeamMin } from '../shared/types/model/TeamMin';
import { State } from '../shared/types/enums/State';
import { QuarterMin } from '../shared/types/model/QuarterMin';
import { CheckInMin } from '../shared/types/model/CheckInMin';
import { KeyResultMetricMin } from '../shared/types/model/KeyResultMetricMin';
import { KeyResultOrdinalMin } from '../shared/types/model/KeyResultOrdinalMin';
import { KeyresultMin } from '../shared/types/model/KeyresultMin';
import { ObjectiveMin } from '../shared/types/model/ObjectiveMin';
import { overViewEntity1 } from '../shared/testData';

describe('TeamComponent', () => {
  let component: TeamComponent;
  let fixture: ComponentFixture<TeamComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TeamComponent, MatIcon],
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
