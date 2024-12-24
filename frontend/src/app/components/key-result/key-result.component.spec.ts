import { ComponentFixture, TestBed } from '@angular/core/testing';
import { KeyResultComponent } from './key-result.component';
import { keyResultMetricMin } from '../../shared/test-data';
import { MatDialogModule } from '@angular/material/dialog';
import { ScoringComponent } from '../../shared/custom/scoring/scoring.component';
import { ConfidenceComponent } from '../confidence/confidence.component';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

describe('KeyResultComponent', () => {
  let component: KeyResultComponent;
  let fixture: ComponentFixture<KeyResultComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [KeyResultComponent,
        ScoringComponent,
        ConfidenceComponent],
      imports: [MatDialogModule],
      providers: [provideHttpClient(withInterceptorsFromDi())]
    })
      .compileComponents();

    fixture = TestBed.createComponent(KeyResultComponent);
    component = fixture.componentInstance;
    component.keyResult = keyResultMetricMin;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
