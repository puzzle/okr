import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectiveRowComponent } from './objective-row.component';

describe('ObjectiveComponent', () => {
  let component: ObjectiveRowComponent;
  let fixture: ComponentFixture<ObjectiveRowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ObjectiveRowComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ObjectiveRowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
