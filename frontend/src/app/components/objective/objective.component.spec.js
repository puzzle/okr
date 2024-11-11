"use strict";
var __assign =
  (this && this.__assign) ||
  function () {
    __assign =
      Object.assign ||
      function (t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
          s = arguments[i];
          for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p)) t[p] = s[p];
        }
        return t;
      };
    return __assign.apply(this, arguments);
  };
var __awaiter =
  (this && this.__awaiter) ||
  function (thisArg, _arguments, P, generator) {
    function adopt(value) {
      return value instanceof P
        ? value
        : new P(function (resolve) {
            resolve(value);
          });
    }
    return new (P || (P = Promise))(function (resolve, reject) {
      function fulfilled(value) {
        try {
          step(generator.next(value));
        } catch (e) {
          reject(e);
        }
      }
      function rejected(value) {
        try {
          step(generator["throw"](value));
        } catch (e) {
          reject(e);
        }
      }
      function step(result) {
        result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected);
      }
      step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
  };
var __generator =
  (this && this.__generator) ||
  function (thisArg, body) {
    var _ = {
        label: 0,
        sent: function () {
          if (t[0] & 1) throw t[1];
          return t[1];
        },
        trys: [],
        ops: [],
      },
      f,
      y,
      t,
      g;
    return (
      (g = { next: verb(0), throw: verb(1), return: verb(2) }),
      typeof Symbol === "function" &&
        (g[Symbol.iterator] = function () {
          return this;
        }),
      g
    );
    function verb(n) {
      return function (v) {
        return step([n, v]);
      };
    }
    function step(op) {
      if (f) throw new TypeError("Generator is already executing.");
      while ((g && ((g = 0), op[0] && (_ = 0)), _))
        try {
          if (
            ((f = 1),
            y &&
              (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) &&
              !(t = t.call(y, op[1])).done)
          )
            return t;
          if (((y = 0), t)) op = [op[0] & 2, t.value];
          switch (op[0]) {
            case 0:
            case 1:
              t = op;
              break;
            case 4:
              _.label++;
              return { value: op[1], done: false };
            case 5:
              _.label++;
              y = op[1];
              op = [0];
              continue;
            case 7:
              op = _.ops.pop();
              _.trys.pop();
              continue;
            default:
              if (!((t = _.trys), (t = t.length > 0 && t[t.length - 1])) && (op[0] === 6 || op[0] === 2)) {
                _ = 0;
                continue;
              }
              if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) {
                _.label = op[1];
                break;
              }
              if (op[0] === 6 && _.label < t[1]) {
                _.label = t[1];
                t = op;
                break;
              }
              if (t && _.label < t[2]) {
                _.label = t[2];
                _.ops.push(op);
                break;
              }
              if (t[2]) _.ops.pop();
              _.trys.pop();
              continue;
          }
          op = body.call(thisArg, _);
        } catch (e) {
          op = [6, e];
          y = 0;
        } finally {
          f = t = 0;
        }
      if (op[0] & 5) throw op[1];
      return { value: op[0] ? op[1] : void 0, done: true };
    }
  };
Object.defineProperty(exports, "__esModule", { value: true });
var testing_1 = require("@angular/core/testing");
var objective_component_1 = require("./objective.component");
var menu_1 = require("@angular/material/menu");
var card_1 = require("@angular/material/card");
var testbed_1 = require("@angular/cdk/testing/testbed");
var animations_1 = require("@angular/platform-browser/animations");
var platform_browser_1 = require("@angular/platform-browser");
var State_1 = require("../../shared/types/enums/State");
var testing_2 = require("@angular/router/testing");
var overview_service_1 = require("../../services/overview.service");
var testData_1 = require("../../shared/testData");
var testing_3 = require("@angular/material/menu/testing");
var keyresult_component_1 = require("../keyresult/keyresult.component");
var dialog_1 = require("@angular/material/dialog");
var testing_4 = require("@angular/common/http/testing");
var icon_1 = require("@angular/material/icon");
var tooltip_1 = require("@angular/material/tooltip");
var scoring_component_1 = require("../../shared/custom/scoring/scoring.component");
var confidence_component_1 = require("../confidence/confidence.component");
var forms_1 = require("@angular/forms");
var de = require("../../../assets/i18n/de.json");
var ngx_translate_testing_1 = require("ngx-translate-testing");
var rxjs_1 = require("rxjs");
var objective_service_1 = require("../../services/objective.service");
var overviewServiceMock = {
  getObjectiveWithKeyresults: jest.fn(),
};
var objectiveServiceMock = {
  getFullObjective: function (objectiveMin) {
    var ongoingObjective = testData_1.objective;
    ongoingObjective.state = State_1.State.ONGOING;
    return (0, rxjs_1.of)(ongoingObjective);
  },
};
describe("ObjectiveColumnComponent", function () {
  var component;
  var fixture;
  var loader;
  beforeEach(function () {
    overviewServiceMock.getObjectiveWithKeyresults.mockReset();
    testing_1.TestBed.configureTestingModule({
      declarations: [
        objective_component_1.ObjectiveComponent,
        keyresult_component_1.KeyresultComponent,
        scoring_component_1.ScoringComponent,
        confidence_component_1.ConfidenceComponent,
        keyresult_component_1.KeyresultComponent,
      ],
      imports: [
        menu_1.MatMenuModule,
        card_1.MatCardModule,
        animations_1.NoopAnimationsModule,
        testing_2.RouterTestingModule,
        dialog_1.MatDialogModule,
        testing_4.HttpClientTestingModule,
        icon_1.MatIconModule,
        tooltip_1.MatTooltipModule,
        forms_1.ReactiveFormsModule,
        ngx_translate_testing_1.TranslateTestingModule.withTranslations({
          de: de,
        }),
      ],
      providers: [
        { provide: overview_service_1.OverviewService, useValue: overviewServiceMock },
        { provide: objective_service_1.ObjectiveService, useValue: objectiveServiceMock },
      ],
    }).compileComponents();
    fixture = testing_1.TestBed.createComponent(objective_component_1.ObjectiveComponent);
    component = fixture.componentInstance;
    loader = testbed_1.TestbedHarnessEnvironment.loader(fixture);
    component.objective = testData_1.objectiveMin;
  });
  it("should create", function () {
    expect(component).toBeTruthy();
  });
  test("Mat-menu should open and close", function () {
    return __awaiter(void 0, void 0, void 0, function () {
      var menu, _a, _b, _c;
      return __generator(this, function (_d) {
        switch (_d.label) {
          case 0:
            component.isWritable = true;
            fixture.detectChanges();
            return [
              4 /*yield*/,
              loader.getHarness(testing_3.MatMenuHarness.with({ selector: '[data-testid="three-dot-menu"]' })),
            ];
          case 1:
            menu = _d.sent();
            _a = expect;
            return [4 /*yield*/, menu.isOpen()];
          case 2:
            _a.apply(void 0, [_d.sent()]).toBeFalsy();
            return [4 /*yield*/, menu.open()];
          case 3:
            _d.sent();
            _b = expect;
            return [4 /*yield*/, menu.isOpen()];
          case 4:
            _b.apply(void 0, [_d.sent()]).toBeTruthy();
            return [4 /*yield*/, menu.close()];
          case 5:
            _d.sent();
            _c = expect;
            return [4 /*yield*/, menu.isOpen()];
          case 6:
            _c.apply(void 0, [_d.sent()]).toBeFalsy();
            return [2 /*return*/];
        }
      });
    });
  });
  test.each([
    [State_1.State.DRAFT, "assets/icons/draft-icon.svg"],
    [State_1.State.ONGOING, "assets/icons/ongoing-icon.svg"],
    [State_1.State.SUCCESSFUL, "assets/icons/successful-icon.svg"],
    [State_1.State.NOTSUCCESSFUL, "assets/icons/not-successful-icon.svg"],
  ])("Status-indicator should change based on the state given by the service", function (state, path) {
    component.objective = __assign(__assign({}, testData_1.objectiveMin), { state: state });
    fixture.detectChanges();
    var image = fixture.debugElement.query(platform_browser_1.By.css('[data-testid="objective-state"]'));
    var statusIndicatorSrc = image.attributes["src"];
    expect(statusIndicatorSrc).toBe(path);
  });
  test("Mat-menu should not be present if writeable is false", function () {
    return __awaiter(void 0, void 0, void 0, function () {
      var menu;
      return __generator(this, function (_a) {
        component.isWritable = false;
        fixture.detectChanges();
        menu = fixture.debugElement.query(platform_browser_1.By.css('[data-testid="objective-menu"]'));
        expect(menu).toBeFalsy();
        return [2 /*return*/];
      });
    });
  });
  test("Create keyresult button should not be present if writeable is false", function () {
    return __awaiter(void 0, void 0, void 0, function () {
      var button;
      return __generator(this, function (_a) {
        component.isWritable = false;
        button = fixture.debugElement.query(platform_browser_1.By.css('[data-testId="add-keyResult"]'));
        expect(button).toBeFalsy();
        return [2 /*return*/];
      });
    });
  });
  it("Correct method should be called when back to draft is clicked", function () {
    jest.spyOn(component, "objectiveBackToDraft");
    component.objective$.next(testData_1.objectiveMin);
    fixture.detectChanges();
    var menuEntry =
      component.getOngoingMenuActions()[
        component
          .getOngoingMenuActions()
          .map(function (menuAction) {
            return menuAction.action;
          })
          .indexOf("todraft")
      ];
    component.handleDialogResult(menuEntry, { endState: "", comment: null, objective: testData_1.objective });
    fixture.detectChanges();
    expect(component.objectiveBackToDraft).toHaveBeenCalled();
  });
  test("Should set isBacklogQuarter right", function () {
    return __awaiter(void 0, void 0, void 0, function () {
      return __generator(this, function (_a) {
        expect(component.isBacklogQuarter).toBeFalsy();
        testData_1.objectiveMin.quarter.label = "Backlog";
        component.objective = testData_1.objectiveMin;
        fixture.detectChanges();
        component.ngOnInit();
        expect(component.isBacklogQuarter).toBeTruthy();
        return [2 /*return*/];
      });
    });
  });
  test("Should return correct menu entries when backlog", function () {
    return __awaiter(void 0, void 0, void 0, function () {
      var menuActions;
      return __generator(this, function (_a) {
        testData_1.objectiveMin.quarter.label = "Backlog";
        component.objective = testData_1.objectiveMin;
        fixture.detectChanges();
        component.ngOnInit();
        menuActions = component.getDraftMenuActions();
        expect(menuActions.length).toEqual(3);
        expect(menuActions[0].displayName).toEqual("Objective bearbeiten");
        expect(menuActions[1].displayName).toEqual("Objective duplizieren");
        expect(menuActions[2].displayName).toEqual("Objective veröffentlichen");
        return [2 /*return*/];
      });
    });
  });
});
