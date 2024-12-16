import { ComponentFixture, TestBed } from "@angular/core/testing";
import { of } from "rxjs";
import { ActivatedRoute } from "@angular/router";
import { TeamService } from "../../services/team.service";
import { TeamListComponent } from "./team-list.component";

describe("TeamListComponent",
  () => {
    let component: TeamListComponent;
    let fixture: ComponentFixture<TeamListComponent>;
    let teamService: TeamService;
    let route: ActivatedRoute;
    const paramTeamId = 1;

    const teamServiceMock = {
      getAllTeams: jest.fn()
        .mockReturnValue(of())
    };

    const activatedRouteMock: any = {
      paramMap: of({
        get: () => paramTeamId
      })
    };

    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [TeamListComponent],
        providers: [{ provide: TeamService,
          useValue: teamServiceMock },
        { provide: ActivatedRoute,
          useValue: activatedRouteMock }]
      })
        .compileComponents();
    });

    beforeEach(() => {
      fixture = TestBed.createComponent(TeamListComponent);
      component = fixture.componentInstance;
      teamService = TestBed.inject(TeamService);
      route = TestBed.inject(ActivatedRoute);
      fixture.detectChanges();
    });

    it("should create",
      () => {
        expect(component)
          .toBeTruthy();
      });

    it("should set selected teamid",
      () => {
        component.ngOnInit();
        expect(component.selectedTeamId)
          .toBe(paramTeamId);
      });

    it("should set selected teamid",
      () => {
        activatedRouteMock.paramMap = of({
          get: () => undefined
        });
        component.ngOnInit();
        expect(component.selectedTeamId)
          .toBe(undefined);
      });
  });
