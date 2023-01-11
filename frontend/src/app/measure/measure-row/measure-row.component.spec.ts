import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MeasureRowComponent } from './measure-row.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import { RouterTestingModule } from '@angular/router/testing';

describe('MeasureRowComponent', () => {
  let component: MeasureRowComponent;
  let fixture: ComponentFixture<MeasureRowComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        BrowserDynamicTestingModule,
        RouterTestingModule,
      ],
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
