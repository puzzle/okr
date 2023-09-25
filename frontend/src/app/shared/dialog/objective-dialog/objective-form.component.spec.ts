import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectiveFormComponent } from './objective-form.component';

describe('ObjectiveDialogComponent', () => {
  let component: ObjectiveFormComponent;
  let fixture: ComponentFixture<ObjectiveFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ObjectiveFormComponent],
    });
    fixture = TestBed.createComponent(ObjectiveFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
