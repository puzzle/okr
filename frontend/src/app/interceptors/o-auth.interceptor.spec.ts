import { TestBed } from '@angular/core/testing';

import { OAuthInterceptor } from './o-auth.interceptor';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { DateTimeProvider, OAuthLogger, OAuthModule, OAuthService, UrlHelperService } from 'angular-oauth2-oidc';

describe('OAuthInterceptor', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [OAuthService, UrlHelperService, OAuthLogger, DateTimeProvider, OAuthService],
    }),
  );

  it('should be created', () => {
    const interceptor: OAuthInterceptor = TestBed.inject(OAuthInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
