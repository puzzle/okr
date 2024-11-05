import { TestBed } from '@angular/core/testing';

import { ObjectiveMenuActionsService } from './objective-menu-actions.service';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

describe('ObjectiveMenuActionsService', () => {
  let service: ObjectiveMenuActionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      providers: [TranslateService, provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ObjectiveMenuActionsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
