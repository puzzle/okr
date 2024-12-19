import { TestBed } from "@angular/core/testing";

import { OverviewService } from "./overview.service";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { of } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { overviews } from "../shared/testData";

const httpClient = {
  get: jest.fn()
};

describe("OverviewService", () => {
  let service: OverviewService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [{ provide: HttpClient,
        useValue: httpClient }]
    })
      .compileComponents();
    service = TestBed.inject(OverviewService);
  });

  it("should be created", () => {
    expect(service)
      .toBeTruthy();
  });

  it("should set state of objectives correctly", (done) => {
    jest.spyOn(httpClient, "get")
      .mockReturnValue(of(overviews));
    service.getOverview()
      .subscribe(() => {
        overviews.forEach((overview) => overview.objectives.forEach((objective) => expect(typeof objective.state)
          .toBe("string")));
        done();
      });
  });
});
