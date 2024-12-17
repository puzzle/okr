import { TeamManagementBannerComponent } from "./team-management-banner.component";
import { ComponentFixture, fakeAsync, TestBed, tick } from "@angular/core/testing";
import { MatDialogModule } from "@angular/material/dialog";
import { of } from "rxjs";
import { AddEditTeamDialogComponent } from "../add-edit-team-dialog/add-edit-team-dialog.component";
import { SearchTeamManagementComponent } from "../search-team-management/search-team-management.component";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { TranslateModule } from "@ngx-translate/core";
import { MatAutocompleteModule } from "@angular/material/autocomplete";
import { ActivatedRoute } from "@angular/router";
import { DialogService } from "../../services/dialog.service";
import { OkrTangramComponent } from "../../shared/custom/okr-tangram/okr-tangram.component";

describe("TeamManagementBannerComponent",
  () => {
    let component: TeamManagementBannerComponent;
    let fixture: ComponentFixture<TeamManagementBannerComponent>;

    const dialogServiceMock = {
      open: jest.fn()
    };

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [
          MatDialogModule,
          MatFormFieldModule,
          MatIconModule,
          HttpClientTestingModule,
          TranslateModule.forRoot(),
          MatAutocompleteModule
        ],
        declarations: [TeamManagementBannerComponent,
          SearchTeamManagementComponent,
          OkrTangramComponent],
        providers: [{ provide: DialogService,
          useValue: dialogServiceMock },
        { provide: ActivatedRoute,
          useValue: {} }]
      })
        .compileComponents();
    });

    beforeEach(() => {
      fixture = TestBed.createComponent(TeamManagementBannerComponent);
      component = fixture.componentInstance;
    });

    it("should create",
      () => {
        expect(component)
          .toBeTruthy();
      });

    it("createTeam should open dialog",
      fakeAsync(() => {
        dialogServiceMock.open.mockReturnValue({
          afterClosed: () => of()
        });
        component.createTeam();
        tick();
        expect(dialogServiceMock.open)
          .toBeCalledTimes(1);
        expect(dialogServiceMock.open)
          .toBeCalledWith(AddEditTeamDialogComponent);
      }));
  });
