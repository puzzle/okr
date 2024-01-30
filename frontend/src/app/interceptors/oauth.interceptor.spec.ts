import { TestBed } from '@angular/core/testing';

import { OauthInterceptor } from './oauth.interceptor';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { DateTimeProvider, OAuthLogger, OAuthModule, OAuthService, UrlHelperService } from 'angular-oauth2-oidc';

describe('OauthInterceptor', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [OAuthService, UrlHelperService, OAuthLogger, DateTimeProvider, OAuthService],
    }),
  );

  it('should be created', () => {
    const interceptor: OauthInterceptor = TestBed.inject(OauthInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
