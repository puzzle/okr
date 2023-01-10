import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectiveDetailComponent } from './objective-detail.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Objective } from '../../shared/services/objective.service';
import { Observable, of } from 'rxjs';
import { KeyResultMeasure } from '../../shared/services/key-result.service';
import { By } from '@angular/platform-browser';
import { ObjectiveModule } from '../objective.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import * as keyresultData from '../../shared/testing/mock-data/keyresults.json';
import * as objectivesData from '../../shared/testing/mock-data/objectives.json';

describe('ObjectiveDetailComponent', () => {
  let component: ObjectiveDetailComponent;
  let fixture: ComponentFixture<ObjectiveDetailComponent>;

  let keyResultList: Observable<KeyResultMeasure[]> = of(
    keyresultData.keyresults
  );

  let objective: Objective = objectivesData.objectives[1];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        ObjectiveModule,
        NoopAnimationsModule,
        RouterTestingModule,
      ],
      declarations: [ObjectiveDetailComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ObjectiveDetailComponent);
    component = fixture.componentInstance;

    component.keyResultList = keyResultList;
    component.objective = objective;

    fixture.detectChanges();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should have cycle with right quarteryear', () => {
    const cycleText = fixture.debugElement.query(By.css('.objective-cycle'));
    expect(cycleText.nativeElement.textContent).toEqual('Zyklus GJ 22/23-Q1 ');
  });

  test('should have owner with right first- and lastname', () => {
    const ownerText = fixture.debugElement.query(By.css('.objective-owner'));
    expect(ownerText.nativeElement.textContent).toEqual(
      'Ziel Besitzer Alice Wunderland '
    );
  });

  test('should have right table titles', () => {
    const spanTextes = fixture.debugElement.queryAll(By.css('span'));
    expect(spanTextes[0].nativeElement.textContent).toEqual('KeyResult');
    expect(spanTextes[1].nativeElement.textContent).toEqual('Besitzer');
    expect(spanTextes[2].nativeElement.textContent).toEqual('Letzte Messung');
    expect(spanTextes[3].nativeElement.textContent).toEqual('Fortschritt');
  });
});
