import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActionPlanComponent } from './action-plan.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('ActionPlanComponent', () => {
  let component: ActionPlanComponent;
  let fixture: ComponentFixture<ActionPlanComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ActionPlanComponent],
      imports: [HttpClientTestingModule],
    });
    fixture = TestBed.createComponent(ActionPlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
