import { TestBed } from '@angular/core/testing';

import { CheckInService } from './check-in.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CheckInMin } from '../types/model/CheckInMin';

describe('CheckInService', () => {
  let service: CheckInService;

  const testedKeyResultId: number = 8;
  const firstCheckIn: CheckInMin = {
    valueMetric: 77,
    confidence: 5,
    zone: undefined,
    changeInfo: '',
    initiatives: '',
    id: 1,
    createdOn: new Date(),
  };
  const secondCheckIn: CheckInMin = {
    valueMetric: 89,
    confidence: 5,
    zone: undefined,
    changeInfo: '',
    initiatives: '',
    id: 2,
    createdOn: new Date(),
  };

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
    service.getAllCheckInOfKeyResult(testedKeyResultId).subscribe((checkIns) => {
      /* Check first CheckIn of this KeyResult */
      expect(checkIns[0].confidence).toBe(firstCheckIn.confidence);
      expect(checkIns[0].valueMetric).toBe(firstCheckIn.valueMetric);
      expect(checkIns[0].zone).toBe(firstCheckIn.zone);

      expect(checkIns[1].confidence).toBe(secondCheckIn.confidence);
      expect(checkIns[1].valueMetric).toBe(secondCheckIn.valueMetric);
      expect(checkIns[1].zone).toBe(secondCheckIn.zone);
    });
  });
});
