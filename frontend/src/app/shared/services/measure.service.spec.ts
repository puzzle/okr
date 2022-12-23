import { TestBed } from '@angular/core/testing';

import { Measure, MeasureService } from './measure.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

const respons = [
  {
    id: 1,
    keyResultId: 1,
    value: 20,
    changeInfo: 'Changeinfo',
    initiatives: 'Initiatives',
    createdBy: 1,
    createdOn: new Date(),
    measureDate: new Date(),
  },
];

describe('MeasureService', () => {
  let service: MeasureService;
  let httpTestingController: HttpTestingController;
  const URL = '/api/v1/measures';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(MeasureService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  test('should get measure by id', (done) => {
    service.getMeasureById(1).subscribe({
      next(response: Measure) {
        expect(response).toEqual(respons);
        done();
      },
      error(error) {
        done(error);
      },
    });

    const req = httpTestingController.expectOne(`${URL}/1`);
    expect(req.request.method).toEqual('GET');
    req.flush(respons);
    httpTestingController.verify();
  });
});
