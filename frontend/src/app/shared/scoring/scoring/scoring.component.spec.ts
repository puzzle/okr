import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScoringComponent } from './scoring.component';
import { MatSliderModule } from '@angular/material/slider';
import { FormsModule } from '@angular/forms';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { keyResultMetric } from '../../testData';
import { HarnessLoader } from '@angular/cdk/testing';

describe('ScoringComponent', () => {
  let component: ScoringComponent;
  let fixture: ComponentFixture<ScoringComponent>;
  let loader: HarnessLoader;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ScoringComponent],
      imports: [MatSliderModule, FormsModule],
    }).compileComponents();

    fixture = TestBed.createComponent(ScoringComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
    component.keyResult = keyResultMetric;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
