import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationTopBarComponent } from './application-top-bar.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { DateTimeProvider, OAuthLogger, OAuthService, UrlHelperService } from 'angular-oauth2-oidc';
import { HttpClient, HttpHandler } from '@angular/common/http';

describe('ApplicationHeaderComponent', () => {
  let component: ApplicationTopBarComponent;
  let fixture: ComponentFixture<ApplicationTopBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ApplicationTopBarComponent],
      providers: [
        { provide: OAuthService },
        { provide: HttpClient },
        { provide: HttpHandler },
        { provide: UrlHelperService },
        { provide: OAuthLogger },
        { provide: DateTimeProvider },
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(ApplicationTopBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
