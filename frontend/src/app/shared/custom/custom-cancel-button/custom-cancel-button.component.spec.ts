import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomCancelButtonComponent } from './custom-cancel-button.component';

describe('CustomCancelButtonComponent', () => {
  let component: CustomCancelButtonComponent;
  let fixture: ComponentFixture<CustomCancelButtonComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CustomCancelButtonComponent],
    });
    fixture = TestBed.createComponent(CustomCancelButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
