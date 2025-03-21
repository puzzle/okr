import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationPageComponent } from './application-page.component';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

describe('ApplicationPageComponent', () => {
  let component: ApplicationPageComponent;
  let fixture: ComponentFixture<ApplicationPageComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [ApplicationPageComponent],
      providers: [provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting()]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ApplicationPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
