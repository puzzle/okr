"use strict";
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
      g = Object.create((typeof Iterator === "function" ? Iterator : Object).prototype);
    return (
      (g.next = verb(0)),
      (g["throw"] = verb(1)),
      (g["return"] = verb(2)),
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
var keyresult_detail_component_1 = require("./keyresult-detail.component");
var testing_2 = require("@angular/common/http/testing");
var dialog_1 = require("@angular/material/dialog");
var core_1 = require("@ngx-translate/core");
var rxjs_1 = require("rxjs");
var testData_1 = require("../../shared/testData");
var platform_browser_1 = require("@angular/platform-browser");
var keyresult_service_1 = require("../../services/keyresult.service");
var icon_1 = require("@angular/material/icon");
var router_1 = require("@angular/router");
var scoring_component_1 = require("../../shared/custom/scoring/scoring.component");
var confidence_component_1 = require("../confidence/confidence.component");
var refresh_data_service_1 = require("../../services/refresh-data.service");
var keyResultServiceMock = {
  getFullKeyResult: jest.fn(),
};
var activatedRouteMock = {
  snapshot: {
    paramMap: {
      get: jest.fn(),
    },
  },
};
describe("KeyresultDetailComponent", function () {
  var component;
  var fixture;
  beforeEach(function () {
    return __awaiter(void 0, void 0, void 0, function () {
      return __generator(this, function (_a) {
        switch (_a.label) {
          case 0:
            return [
              4 /*yield*/,
              testing_1.TestBed.configureTestingModule({
                imports: [
                  testing_2.HttpClientTestingModule,
                  dialog_1.MatDialogModule,
                  icon_1.MatIconModule,
                  core_1.TranslateModule.forRoot(),
                ],
                declarations: [
                  keyresult_detail_component_1.KeyresultDetailComponent,
                  scoring_component_1.ScoringComponent,
                  confidence_component_1.ConfidenceComponent,
                ],
                providers: [
                  {
                    provide: keyresult_service_1.KeyresultService,
                    useValue: keyResultServiceMock,
                  },
                  {
                    provide: router_1.ActivatedRoute,
                    useValue: activatedRouteMock,
                  },
                ],
              }).compileComponents(),
            ];
          case 1:
            _a.sent();
            jest.spyOn(keyResultServiceMock, "getFullKeyResult").mockReturnValue((0, rxjs_1.of)(testData_1.keyResult));
            activatedRouteMock.snapshot.paramMap.get.mockReturnValue((0, rxjs_1.of)(1));
            fixture = testing_1.TestBed.createComponent(keyresult_detail_component_1.KeyresultDetailComponent);
            component = fixture.componentInstance;
            fixture.detectChanges();
            return [2 /*return*/];
        }
      });
    });
  });
  it("should create", function () {
    expect(component).toBeTruthy();
  });
  it("should throw error when id is undefined", function () {
    activatedRouteMock.snapshot.paramMap.get.mockReturnValue(undefined);
    expect(function () {
      return component.ngOnInit();
    }).toThrowError("keyresult id is undefined");
  });
  it("should display edit keyresult button if writeable is true", function () {
    return __awaiter(void 0, void 0, void 0, function () {
      var button;
      return __generator(this, function (_a) {
        button = fixture.debugElement.query(platform_browser_1.By.css('[data-testId="edit-keyResult"]'));
        expect(button).toBeTruthy();
        return [2 /*return*/];
      });
    });
  });
  it("should not display edit keyresult button if writeable is false", function () {
    return __awaiter(void 0, void 0, void 0, function () {
      var button;
      return __generator(this, function (_a) {
        jest
          .spyOn(keyResultServiceMock, "getFullKeyResult")
          .mockReturnValue((0, rxjs_1.of)(testData_1.keyResultWriteableFalse));
        component.ngOnInit();
        fixture.detectChanges();
        button = fixture.debugElement.query(platform_browser_1.By.css('[data-testId="edit-keyResult"]'));
        expect(button).toBeFalsy();
        return [2 /*return*/];
      });
    });
  });
  it("should display add check-in button if writeable is true", function () {
    return __awaiter(void 0, void 0, void 0, function () {
      var button;
      return __generator(this, function (_a) {
        button = fixture.debugElement.query(platform_browser_1.By.css('[data-testId="add-check-in"]'));
        expect(button).toBeTruthy();
        return [2 /*return*/];
      });
    });
  });
  it("should not display add check-in button if writeable is false", function () {
    return __awaiter(void 0, void 0, void 0, function () {
      var button;
      return __generator(this, function (_a) {
        jest
          .spyOn(keyResultServiceMock, "getFullKeyResult")
          .mockReturnValue((0, rxjs_1.of)(testData_1.keyResultWriteableFalse));
        component.ngOnInit();
        fixture.detectChanges();
        button = fixture.debugElement.query(platform_browser_1.By.css('[data-testId="add-check-in"]'));
        expect(button).toBeFalsy();
        return [2 /*return*/];
      });
    });
  });
  it("should trigger observable when subject gets next value", function () {
    var spy = jest.spyOn(component, "loadKeyResult");
    var refreshDataService = testing_1.TestBed.inject(refresh_data_service_1.RefreshDataService);
    refreshDataService.reloadKeyResultSubject.next();
    expect(spy).toHaveBeenCalled();
  });
  it("should close subscription on destroy", function () {
    var spyNext = jest.spyOn(component.ngDestroy$, "next");
    var spyComplete = jest.spyOn(component.ngDestroy$, "complete");
    component.ngOnDestroy();
    expect(spyNext).toHaveBeenCalled();
    expect(spyComplete).toHaveBeenCalled();
  });
});
