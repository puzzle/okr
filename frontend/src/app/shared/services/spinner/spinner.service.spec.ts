import { TestBed } from '@angular/core/testing';

import { SpinnerService } from './spinner.service';

describe('SpinnerService', () => {
  let service: SpinnerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SpinnerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
    expect(service.visibility.value).toBe(false);
  });

  it('should switch visibility', () => {
    service.show();
    expect(service.visibility.value).toBe(true);
    service.hide();
    expect(service.visibility.value).toBe(false);
  });
});
