"use strict";
var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (g && (g = 0, op[0] && (_ = 0)), _) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
Object.defineProperty(exports, "__esModule", { value: true });
var testing_1 = require("@angular/core/testing");
var team_component_1 = require("./team.component");
var icon_1 = require("@angular/material/icon");
var testData_1 = require("../../shared/testData");
var objective_component_1 = require("../objective/objective.component");
var testing_2 = require("@angular/router/testing");
var menu_1 = require("@angular/material/menu");
var keyresult_component_1 = require("../keyresult/keyresult.component");
var dialog_1 = require("@angular/material/dialog");
var testing_3 = require("@angular/common/http/testing");
var platform_browser_1 = require("@angular/platform-browser");
var refresh_data_service_1 = require("../../services/refresh-data.service");
var ngx_translate_testing_1 = require("ngx-translate-testing");
// @ts-ignore
var de = require("../../../assets/i18n/de.json");
var tooltip_1 = require("@angular/material/tooltip");
var confidence_component_1 = require("../confidence/confidence.component");
var scoring_component_1 = require("../../shared/custom/scoring/scoring.component");
var core_1 = require("@angular/core");
var dialog_service_1 = require("../../services/dialog.service");
var dialogService = {
    open: jest.fn(),
};
var refreshDataServiceMock = {
    markDataRefresh: jest.fn(),
};
describe('TeamComponent', function () {
    var component;
    var fixture;
    beforeEach(function () { return __awaiter(void 0, void 0, void 0, function () {
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0: return [4 /*yield*/, testing_1.TestBed.configureTestingModule({
                        imports: [
                            testing_2.RouterTestingModule,
                            menu_1.MatMenuModule,
                            dialog_1.MatDialogModule,
                            testing_3.HttpClientTestingModule,
                            tooltip_1.MatTooltipModule,
                            ngx_translate_testing_1.TranslateTestingModule.withTranslations({
                                de: de,
                            }),
                            icon_1.MatIcon,
                        ],
                        declarations: [team_component_1.TeamComponent, objective_component_1.ObjectiveComponent, keyresult_component_1.KeyresultComponent, confidence_component_1.ConfidenceComponent, scoring_component_1.ScoringComponent],
                        providers: [
                            {
                                provide: dialog_service_1.DialogService,
                                useValue: dialogService,
                            },
                            {
                                provide: refresh_data_service_1.RefreshDataService,
                                useValue: refreshDataServiceMock,
                            },
                        ],
                    })
                        .overrideComponent(team_component_1.TeamComponent, {
                        set: { changeDetection: core_1.ChangeDetectionStrategy.Default },
                    })
                        .compileComponents()];
                case 1:
                    _a.sent();
                    fixture = testing_1.TestBed.createComponent(team_component_1.TeamComponent);
                    component = fixture.componentInstance;
                    component.overviewEntity = testData_1.overViewEntity1;
                    fixture.detectChanges();
                    return [2 /*return*/];
            }
        });
    }); });
    it('should create', function () {
        fixture.detectChanges();
        expect(component).toBeTruthy();
    });
    it('should display add objective button if writeable is true', function () { return __awaiter(void 0, void 0, void 0, function () {
        var button;
        return __generator(this, function (_a) {
            fixture.detectChanges();
            button = fixture.debugElement.query(platform_browser_1.By.css('[data-testId="add-objective"]'));
            expect(button).toBeTruthy();
            return [2 /*return*/];
        });
    }); });
    it('should not display add objective button if writeable is false', function () {
        component.overviewEntity = __assign({}, testData_1.overViewEntity2);
        component.overviewEntity.writeable = false;
        fixture.detectChanges();
        var button = fixture.debugElement.query(platform_browser_1.By.css('[data-testId="add-objective"]'));
        expect(button).toBeFalsy();
    });
    it('should display right add objective icon', function () {
        component.overviewEntity = __assign({}, testData_1.overViewEntity2);
        component.overviewEntity.writeable = true;
        component.ngOnInit();
        expect(component.addIconSrc.value).toBe('/assets/icons/new-icon.svg');
    });
});
//# sourceMappingURL=team.component.spec.js.map