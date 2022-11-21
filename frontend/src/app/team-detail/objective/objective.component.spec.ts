import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectiveComponent } from './objective.component';

describe('ObjectiveComponent', () => {
  let component: ObjectiveComponent;
  let fixture: ComponentFixture<ObjectiveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ObjectiveComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ObjectiveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
