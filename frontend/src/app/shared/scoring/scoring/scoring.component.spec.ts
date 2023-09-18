import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScoringComponent } from './scoring.component';

describe('ScoringComponent', () => {
  let component: ScoringComponent;
  let fixture: ComponentFixture<ScoringComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ScoringComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ScoringComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
