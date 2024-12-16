import { ComponentFixture, TestBed } from "@angular/core/testing";
import { TeamComponent } from "./team.component";
import { MatIcon } from "@angular/material/icon";
import { overViewEntity1, overViewEntity2 } from "../../shared/testData";
import { ObjectiveComponent } from "../objective/objective.component";
import { RouterTestingModule } from "@angular/router/testing";
import { MatMenuModule } from "@angular/material/menu";
import { KeyresultComponent } from "../keyresult/keyresult.component";
import { MatDialogModule } from "@angular/material/dialog";
import { By } from "@angular/platform-browser";
import { RefreshDataService } from "../../services/refresh-data.service";
import { TranslateTestingModule } from "ngx-translate-testing";
// @ts-ignore
import * as de from "../../../assets/i18n/de.json";
import { MatTooltipModule } from "@angular/material/tooltip";
import { ConfidenceComponent } from "../confidence/confidence.component";
import { ScoringComponent } from "../../shared/custom/scoring/scoring.component";
import { ChangeDetectionStrategy } from "@angular/core";
import { DialogService } from "../../services/dialog.service";
import { ConfigService } from "../../services/config.service";
import { of } from "rxjs";
import { provideHttpClient, withInterceptorsFromDi } from "@angular/common/http";

const dialogService = {
  open: jest.fn()
};

const configServiceDefined = {
  config$: of({
    customStyles: {
      "okr-add-objective-icon": "new-icon-from-config-service.svg"
    }
  })
};

const configServiceUndefined = {
  config$: of({
    customStyles: {
      "okr-add-objective-icon": undefined
    }
  })
};

const refreshDataServiceMock = {
  markDataRefresh: jest.fn()
};

describe("TeamComponent",
  () => {
    let component: TeamComponent;
    let fixture: ComponentFixture<TeamComponent>;

    beforeEach(async () => {
      await TestBed.configureTestingModule({
        imports: [
          RouterTestingModule,
          MatMenuModule,
          MatDialogModule,
          MatTooltipModule,
          TranslateTestingModule.withTranslations({
            de: de
          }),
          MatIcon
        ],
        declarations: [
          TeamComponent,
          ObjectiveComponent,
          KeyresultComponent,
          ConfidenceComponent,
          ScoringComponent
        ],
        providers: [
          {
            provide: DialogService,
            useValue: dialogService
          },
          {
            provide: ConfigService,
            useValue: configServiceDefined
          },
          {
            provide: RefreshDataService,
            useValue: refreshDataServiceMock
          },
          provideHttpClient(withInterceptorsFromDi())
        ]
      })
        .overrideComponent(TeamComponent,
          {
            set: { changeDetection: ChangeDetectionStrategy.Default }
          })
        .compileComponents();

      fixture = TestBed.createComponent(TeamComponent);
      component = fixture.componentInstance;
      component.overviewEntity = overViewEntity1;
      fixture.detectChanges();
    });

    it("should create",
      () => {
        fixture.detectChanges();
        expect(component)
          .toBeTruthy();
      });

    it("should display add objective button if writeable is true",
      async () => {
        fixture.detectChanges();
        const button = fixture.debugElement.query(By.css("[data-testId=\"add-objective\"]"));
        expect(button)
          .toBeTruthy();
      });

    it("should not display add objective button if writeable is false",
      () => {
        component.overviewEntity = { ...overViewEntity2 };
        component.overviewEntity.writeable = false;
        fixture.detectChanges();
        const button = fixture.debugElement.query(By.css("[data-testId=\"add-objective\"]"));
        expect(button)
          .toBeFalsy();
      });

    it("should set value of addIconSrc if src from config service is defined",
      () => {
        expect(component.addIconSrc.value)
          .toBe("new-icon-from-config-service.svg");
        const addObjectiveIcon = fixture.debugElement.query(By.css("[data-testId=\"add-objective-icon\"]"));
        expect(addObjectiveIcon.attributes["src"])
          .toBe("new-icon-from-config-service.svg");
      });
  });

describe("TeamComponent undefined values in config service",
  () => {
    let component: TeamComponent;
    let fixture: ComponentFixture<TeamComponent>;

    beforeEach(async () => {
      await TestBed.configureTestingModule({
        imports: [MatMenuModule,
          TranslateTestingModule.withTranslations({
            de: de
          })],
        declarations: [TeamComponent],
        providers: [{
          provide: ConfigService,
          useValue: configServiceUndefined
        },
        provideHttpClient(withInterceptorsFromDi())]
      })
        .overrideComponent(TeamComponent,
          {
            set: { changeDetection: ChangeDetectionStrategy.Default }
          })
        .compileComponents();

      fixture = TestBed.createComponent(TeamComponent);
      component = fixture.componentInstance;
      component.overviewEntity = overViewEntity1;
      fixture.detectChanges();
    });

    it("should keep default value of addIconSrc if src from config service is undefined",
      () => {
        expect(component.addIconSrc.value)
          .toBe("assets/icons/new-icon.svg");
        const addObjectiveIcon = fixture.debugElement.query(By.css("[data-testId=\"add-objective-icon\"]"));
        expect(addObjectiveIcon.attributes["src"])
          .toBe("assets/icons/new-icon.svg");
      });
  });
