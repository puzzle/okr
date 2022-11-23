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
    if (currentQuarter == 4) {
      expect(cycleList[5].cycle).toEqual(
        (currentYear + 1).toString().slice(-2) + '-' + 1
      );
    } else {
      expect(cycleList[5].cycle).toEqual(
        currentYear.toString().slice(-2) + '-' + (currentQuarter + 1)
      );
    }
  });

  it('should return first quarter at last march', () => {
    let firstQuarter = new Date('2022-03-31');
    let cycleList = service.getQuarter(firstQuarter);
    expect(cycleList.length).toEqual(6);
    expect(cycleList[0].cycle).toEqual('22-1');
    expect(cycleList[1].cycle).toEqual('21-4');
    expect(cycleList[5].cycle).toEqual('22-2');
  });

  it('should return second quarter at first april', () => {
    let secondQuarter = new Date('2022-04-01');
    let cycleList = service.getQuarter(secondQuarter);
    expect(cycleList.length).toEqual(6);
    expect(cycleList[0].cycle).toEqual('22-2');
    expect(cycleList[1].cycle).toEqual('22-1');
    expect(cycleList[5].cycle).toEqual('22-3');
  });

  it('should return right cycles at beginning of year', () => {
    let beginningOfYear = new Date('2022-01-01');
    let cycleList = service.getQuarter(beginningOfYear);
    expect(cycleList.length).toEqual(6);
    expect(cycleList[0].cycle).toEqual('22-1');
    expect(cycleList[1].cycle).toEqual('21-4');
    expect(cycleList[2].cycle).toEqual('21-3');
    expect(cycleList[3].cycle).toEqual('21-2');
    expect(cycleList[4].cycle).toEqual('21-1');
    expect(cycleList[5].cycle).toEqual('22-2');
  });

  it('should return right cycles in the middle of year', () => {
    let middleOfYear = new Date('2018-06-15');
    let cycleList = service.getQuarter(middleOfYear);
    expect(cycleList.length).toEqual(6);
    expect(cycleList[0].cycle).toEqual('18-2');
    expect(cycleList[1].cycle).toEqual('18-1');
    expect(cycleList[2].cycle).toEqual('17-4');
    expect(cycleList[3].cycle).toEqual('17-3');
    expect(cycleList[4].cycle).toEqual('17-2');
    expect(cycleList[5].cycle).toEqual('18-3');
  });

  it('should return right cycles at end of year', () => {
    let endOfYear = new Date('2010-12-31');
    let cycleList = service.getQuarter(endOfYear);
    expect(cycleList.length).toEqual(6);
    expect(cycleList[0].cycle).toEqual('10-4');
    expect(cycleList[1].cycle).toEqual('10-3');
    expect(cycleList[2].cycle).toEqual('10-2');
    expect(cycleList[3].cycle).toEqual('10-1');
    expect(cycleList[4].cycle).toEqual('09-4');
    expect(cycleList[5].cycle).toEqual('11-1');
  });
});
