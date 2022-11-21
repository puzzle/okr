import { TestBed } from '@angular/core/testing';
import { TeamService } from './team.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('TeamService', () => {
  let service: TeamService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(TeamService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return 6 cycles', () => {
    let currentYear = new Date().getFullYear();
    let currentQuarter: number = Math.floor(new Date().getMonth() / 3 + 1);
    let cycleList = service.getQuarter();
    expect(cycleList.length).toEqual(6);
    expect(cycleList[0].cycle).toEqual(
      currentYear.toString().slice(-2) + '-' + currentQuarter
    );
  });
});
