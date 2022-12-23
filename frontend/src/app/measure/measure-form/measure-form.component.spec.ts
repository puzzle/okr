import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MeasureFormComponent } from './measure-form.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { MatIconModule } from '@angular/material/icon';

describe('MeasureFormComponent', () => {
  let component: MeasureFormComponent;
  let fixture: ComponentFixture<MeasureFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeasureFormComponent],
      imports: [HttpClientTestingModule, RouterTestingModule, MatIconModule],
    }).compileComponents();

    fixture = TestBed.createComponent(MeasureFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
