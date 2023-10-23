import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomPrimaryButtonComponent } from './custom-primary-button.component';

describe('CustomPrimaryButtonComponent', () => {
  let component: CustomPrimaryButtonComponent;
  let fixture: ComponentFixture<CustomPrimaryButtonComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CustomPrimaryButtonComponent],
    });
    fixture = TestBed.createComponent(CustomPrimaryButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
