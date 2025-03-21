import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StatisticsInformationComponent } from './statistics-information.component';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

describe('StatisticsInformationComponent', () => {
  let component: StatisticsInformationComponent;
  let fixture: ComponentFixture<StatisticsInformationComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [StatisticsInformationComponent],
      providers: [provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting()]
    })
      .compileComponents();

    fixture = TestBed.createComponent(StatisticsInformationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
