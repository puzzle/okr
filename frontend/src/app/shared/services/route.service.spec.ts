import { TestBed } from '@angular/core/testing';

import { RouteService } from './route.service';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

describe('RouteService', () => {
  let service: RouteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            paramMap: of(
              convertToParamMap({ objectives: '1,2', keyresults: '1' })
            ),
          },
        },
      ],
    });
    service = TestBed.inject(RouteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
