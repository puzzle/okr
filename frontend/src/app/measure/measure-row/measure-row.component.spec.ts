import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MeasureRowComponent } from './measure-row.component';

describe('MeasureRowComponent', () => {
  let component: MeasureRowComponent;
  let fixture: ComponentFixture<MeasureRowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeasureRowComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(MeasureRowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
