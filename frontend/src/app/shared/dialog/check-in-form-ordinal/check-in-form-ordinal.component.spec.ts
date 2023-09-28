import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckInFormOrdinalComponent } from './check-in-form-ordinal.component';

describe('CheckInFormOrdinalComponent', () => {
  let component: CheckInFormOrdinalComponent;
  let fixture: ComponentFixture<CheckInFormOrdinalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CheckInFormOrdinalComponent]
    });
    fixture = TestBed.createComponent(CheckInFormOrdinalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
