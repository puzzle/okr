import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfidenceComponent } from './confidence.component';

describe('ConfidenceComponent', () => {
  let component: ConfidenceComponent;
  let fixture: ComponentFixture<ConfidenceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ConfidenceComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ConfidenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
