import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckInBaseInformationsComponent } from './check-in-base-informations.component';

describe('CheckInBaseInformationsComponent', () => {
  let component: CheckInBaseInformationsComponent;
  let fixture: ComponentFixture<CheckInBaseInformationsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CheckInBaseInformationsComponent]
    });
    fixture = TestBed.createComponent(CheckInBaseInformationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
