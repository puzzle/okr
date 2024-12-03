import { ComponentFixture, TestBed } from '@angular/core/testing';
import { KeyresultComponent } from './keyresult.component';
import { keyResultMetricMin } from '../../shared/testData';
import { MatDialogModule } from '@angular/material/dialog';
import { ScoringComponent } from '../../shared/custom/scoring/scoring.component';
import { ConfidenceComponent } from '../confidence/confidence.component';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

describe('KeyresultComponent', () => {
  let component: KeyresultComponent;
  let fixture: ComponentFixture<KeyresultComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [KeyresultComponent, ScoringComponent, ConfidenceComponent],
      imports: [MatDialogModule],
      providers: [provideHttpClient(withInterceptorsFromDi())],
    }).compileComponents();

    fixture = TestBed.createComponent(KeyresultComponent);
    component = fixture.componentInstance;
    component.keyResult = keyResultMetricMin;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
