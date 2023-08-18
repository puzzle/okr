import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectiveColumnComponent } from './objective-column.component';

describe('ObjectiveColumnComponent', () => {
  let component: ObjectiveColumnComponent;
  let fixture: ComponentFixture<ObjectiveColumnComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ObjectiveColumnComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ObjectiveColumnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
