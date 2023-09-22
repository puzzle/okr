import { TestBed } from '@angular/core/testing';

import { DrawerInterceptor } from './drawer.interceptor';

describe('DrawerInterceptor', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      providers: [DrawerInterceptor],
    }),
  );

  it('should be created', () => {
    const interceptor: DrawerInterceptor = TestBed.inject(DrawerInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
