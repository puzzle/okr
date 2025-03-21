import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OverviewBannerComponent } from './overview-banner.component';
import { ApplicationBannerComponent } from '../../shared/custom/application-banner/application-banner.component';
import { QuarterFilterComponent } from '../../shared/filter/quarter-filter/quarter-filter.component';
import { TeamFilterComponent } from '../../shared/filter/team-filter/team-filter.component';
import { ObjectiveFilterComponent } from '../objective-filter/objective-filter.component';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatChipsModule } from '@angular/material/chips';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';


describe('OverviewBannerComponent', () => {
  let component: OverviewBannerComponent;
  let fixture: ComponentFixture<OverviewBannerComponent>;
  window.ResizeObserver =
      window.ResizeObserver ||
      jest.fn()
        .mockImplementation(() => ({
          disconnect: jest.fn(),
          observe: jest.fn(),
          unobserve: jest.fn()
        }));

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [
        OverviewBannerComponent,
        ApplicationBannerComponent,
        QuarterFilterComponent,
        TeamFilterComponent,
        ObjectiveFilterComponent
      ],
      providers: [provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting()],
      imports: [
        MatExpansionModule,
        MatSelectModule,
        MatChipsModule,
        FormsModule,
        MatFormFieldModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(OverviewBannerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
