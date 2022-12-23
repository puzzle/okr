import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MeasureFormComponent } from './measure-form.component';

describe('MeasureFormComponent', () => {
  let component: MeasureFormComponent;
  let fixture: ComponentFixture<MeasureFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeasureFormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(MeasureFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
