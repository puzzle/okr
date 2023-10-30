import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationTopBarComponent } from './application-top-bar.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { DateTimeProvider, OAuthLogger, OAuthService, UrlHelperService } from 'angular-oauth2-oidc';
import { HttpClient, HttpHandler } from '@angular/common/http';

const oAuthMock = {
  getIdentityClaims: jest.fn(),
  logOut: jest.fn(),
};

describe('ApplicationHeaderComponent', () => {
  let component: ApplicationTopBarComponent;
  let fixture: ComponentFixture<ApplicationTopBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ApplicationTopBarComponent],
      providers: [
        { provide: OAuthService, useValue: oAuthMock },
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
    oAuthMock.getIdentityClaims.mockReturnValue({ name: 'Firstname Lastname' });
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display name correctly', () => {
    const name = document.querySelector("pzsh-menu-dropdown>div[slot='toggle']")?.textContent;
    expect(name).toContain('Firstname Lastname');
  });

  it('logout function should get called on button click', () => {
    const dropdown: HTMLElement = document.querySelector('pzsh-menu-dropdown')!;
    dropdown.click();
    fixture.detectChanges();
    const logOutButton: HTMLElement = document.querySelector('pzsh-menu-dropdown-item')!;
    logOutButton.click();
    expect(oAuthMock.logOut).toBeCalledTimes(1);
  });
});
