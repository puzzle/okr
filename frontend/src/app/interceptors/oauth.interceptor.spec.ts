import { TestBed } from '@angular/core/testing';

import { OauthInterceptor } from './oauth.interceptor';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { OidcSecurityService, StsConfigLoader } from 'angular-auth-oidc-client';
import { of } from 'rxjs';

describe('OauthInterceptor', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        OidcSecurityService,
        {
          provide: StsConfigLoader,
          useValue: {
            loadConfig: () => of({}),
            loadConfigs: () => of([{}]),
          },
        },
      ],
    }),
  );

  it('should be created', () => {
    const interceptor: OauthInterceptor = TestBed.inject(OauthInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
