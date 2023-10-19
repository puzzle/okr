import { PreserveQueryParamsPathLocationStrategy } from './preserveQueryParamsPathLocationStrategy';
import { TestBed } from '@angular/core/testing';
import { PlatformLocation } from '@angular/common';
import { RouterTestingModule } from '@angular/router/testing';

const platformLocationMock = {
  search: jest.fn(),
};
describe('preserveQueryParamsPathLocationStrategy', () => {
  let service: PreserveQueryParamsPathLocationStrategy;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [
        PreserveQueryParamsPathLocationStrategy,
        {
          provide: PlatformLocation,
          useValue: platformLocationMock,
        },
      ],
    });
    service = TestBed.inject(PreserveQueryParamsPathLocationStrategy);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it.each([
    ['?teams=5,6&quarter=7', '/?teams=5,6,8', '/?teams=5,6,8&quarter=7'],
    ['?teams=5,6&quarter=7', '/?teams=', '/?teams=5,6,8&quarter=7'],
    ['?teams=5,6&quarter=7', '/?teams=5,6,8', '/?teams=5,6,8&quarter=7'],
  ])('should map correctly', (current: string, next: string, expected: string) => {
    platformLocationMock.search.mockReturnValue(current);
    const mergedParams = service.prepareExternalUrl(next);
    expect(mergedParams).toBe(expected);
  });
});
