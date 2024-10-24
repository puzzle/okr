import { TestBed } from '@angular/core/testing';

import { ObjectiveMenuActionsService } from './objective-menu-actions.service';

describe('ObjectiveMenuActionsService', () => {
  let service: ObjectiveMenuActionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ObjectiveMenuActionsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
