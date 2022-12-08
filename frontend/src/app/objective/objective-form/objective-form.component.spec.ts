import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectiveFormComponent } from './objective-form.component';

describe('ObjectiveFormComponent', () => {
  let component: ObjectiveFormComponent;
  let fixture: ComponentFixture<ObjectiveFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ObjectiveFormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ObjectiveFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
