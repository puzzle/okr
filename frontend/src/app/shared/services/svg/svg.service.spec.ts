import { TestBed } from '@angular/core/testing';

import { SvgService } from './svg.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

const response = 'svg code';

describe('SvgService', () => {
  let service: SvgService;
  let httpTestingController: HttpTestingController;
  const URL = '../../assets/images';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(SvgService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  test('should get svg', (done) => {
    service.getSvg('progress_bar.svg').subscribe({
      next(response: string) {
        expect(response).toEqual(response);
        done();
      },
      error(error) {
        done(error);
      },
    });

    const req = httpTestingController.expectOne(`${URL}/progress_bar.svg`);
    expect(req.request.method).toEqual('GET');
    req.flush(response);
    httpTestingController.verify();
  });
});
