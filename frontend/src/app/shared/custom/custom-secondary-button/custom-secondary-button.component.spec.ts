import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomSecondaryButtonComponent } from './custom-secondary-button.component';

describe('CustomSecondaryButtonComponent', () => {
  let component: CustomSecondaryButtonComponent;
  let fixture: ComponentFixture<CustomSecondaryButtonComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CustomSecondaryButtonComponent],
    });
    fixture = TestBed.createComponent(CustomSecondaryButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
