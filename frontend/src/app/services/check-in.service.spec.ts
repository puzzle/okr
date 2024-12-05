import { TestBed } from '@angular/core/testing';

import { CheckInService } from './check-in.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { firstCheckIn, keyResultMetricWithIdEight, secondCheckIn } from '../shared/testData';
import {CheckInMetricMin} from "../shared/types/model/CheckInMetricMin";

describe('CheckInService', () => {
  let service: CheckInService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(CheckInService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should map correctly', () => {
    service.getAllCheckInOfKeyResult(keyResultMetricWithIdEight.id).subscribe((checkIns) => {
      /* Check first CheckIn of this KeyResult */
      expect(checkIns[0].confidence).toBe(firstCheckIn.confidence);
      expect((checkIns[0] as CheckInMetricMin).value).toBe(firstCheckIn.value);

      expect(checkIns[1].confidence).toBe(secondCheckIn.confidence);
      expect((checkIns[1] as CheckInMetricMin).value).toBe(secondCheckIn.value);
    });
  });
});
