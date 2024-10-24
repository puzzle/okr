"use strict";
var __esDecorate =
  (this && this.__esDecorate) ||
  function (ctor, descriptorIn, decorators, contextIn, initializers, extraInitializers) {
    function accept(f) {
      if (f !== void 0 && typeof f !== "function") throw new TypeError("Function expected");
      return f;
    }
    var kind = contextIn.kind,
      key = kind === "getter" ? "get" : kind === "setter" ? "set" : "value";
    var target = !descriptorIn && ctor ? (contextIn["static"] ? ctor : ctor.prototype) : null;
    var descriptor = descriptorIn || (target ? Object.getOwnPropertyDescriptor(target, contextIn.name) : {});
    var _,
      done = false;
    for (var i = decorators.length - 1; i >= 0; i--) {
      var context = {};
      for (var p in contextIn) context[p] = p === "access" ? {} : contextIn[p];
      for (var p in contextIn.access) context.access[p] = contextIn.access[p];
      context.addInitializer = function (f) {
        if (done) throw new TypeError("Cannot add initializers after decoration has completed");
        extraInitializers.push(accept(f || null));
      };
      var result = (0, decorators[i])(
        kind === "accessor" ? { get: descriptor.get, set: descriptor.set } : descriptor[key],
        context,
      );
      if (kind === "accessor") {
        if (result === void 0) continue;
        if (result === null || typeof result !== "object") throw new TypeError("Object expected");
        if ((_ = accept(result.get))) descriptor.get = _;
        if ((_ = accept(result.set))) descriptor.set = _;
        if ((_ = accept(result.init))) initializers.unshift(_);
      } else if ((_ = accept(result))) {
        if (kind === "field") initializers.unshift(_);
        else descriptor[key] = _;
      }
    }
    if (target) Object.defineProperty(target, contextIn.name, descriptor);
    done = true;
  };
var __runInitializers =
  (this && this.__runInitializers) ||
  function (thisArg, initializers, value) {
    var useValue = arguments.length > 2;
    for (var i = 0; i < initializers.length; i++) {
      value = useValue ? initializers[i].call(thisArg, value) : initializers[i].call(thisArg);
    }
    return useValue ? value : void 0;
  };
var __setFunctionName =
  (this && this.__setFunctionName) ||
  function (f, name, prefix) {
    if (typeof name === "symbol") name = name.description ? "[".concat(name.description, "]") : "";
    return Object.defineProperty(f, "name", {
      configurable: true,
      value: prefix ? "".concat(prefix, " ", name) : name,
    });
  };
Object.defineProperty(exports, "__esModule", { value: true });
exports.KeyresultDetailComponent = void 0;
var core_1 = require("@angular/core");
var check_in_history_dialog_component_1 = require("../check-in-history-dialog/check-in-history-dialog.component");
var rxjs_1 = require("rxjs");
var CloseState_1 = require("../../shared/types/enums/CloseState");
var check_in_form_component_1 = require("../checkin/check-in-form/check-in-form.component");
var constantLibary_1 = require("../../shared/constantLibary");
var common_1 = require("../../shared/common");
var keyresult_dialog_component_1 = require("../keyresult-dialog/keyresult-dialog.component");
var confirm_dialog_component_1 = require("../../shared/dialog/confirm-dialog/confirm-dialog.component");
var KeyresultDetailComponent = (function () {
  var _classDecorators = [
    (0, core_1.Component)({
      selector: "app-keyresult-detail",
      templateUrl: "./keyresult-detail.component.html",
      styleUrls: ["./keyresult-detail.component.scss"],
      changeDetection: core_1.ChangeDetectionStrategy.OnPush,
    }),
  ];
  var _classDescriptor;
  var _classExtraInitializers = [];
  var _classThis;
  var _keyResultId_decorators;
  var _keyResultId_initializers = [];
  var _keyResultId_extraInitializers = [];
  var KeyresultDetailComponent = (_classThis = /** @class */ (function () {
    function KeyresultDetailComponent_1(keyResultService, refreshDataService, dialog, router, route) {
      this.keyResultService = keyResultService;
      this.refreshDataService = refreshDataService;
      this.dialog = dialog;
      this.router = router;
      this.route = route;
      this.keyResultId = __runInitializers(this, _keyResultId_initializers, void 0);
      this.keyResult$ = (__runInitializers(this, _keyResultId_extraInitializers), new rxjs_1.BehaviorSubject({}));
      this.ngDestroy$ = new rxjs_1.Subject();
      this.isComplete = false;
      this.DATE_FORMAT = constantLibary_1.DATE_FORMAT;
      this.isLastCheckInNegative = common_1.isLastCheckInNegative;
      this.calculateCurrentPercentage = common_1.calculateCurrentPercentage;
    }
    KeyresultDetailComponent_1.prototype.ngOnInit = function () {
      var _this = this;
      this.keyResultId = this.getIdFromParams();
      this.loadKeyResult(this.keyResultId);
      this.refreshDataService.reloadKeyResultSubject
        .pipe((0, rxjs_1.takeUntil)(this.ngDestroy$))
        .subscribe(function () {
          _this.loadKeyResult(_this.keyResultId);
        });
    };
    KeyresultDetailComponent_1.prototype.ngOnDestroy = function () {
      this.ngDestroy$.next();
      this.ngDestroy$.complete();
    };
    KeyresultDetailComponent_1.prototype.getIdFromParams = function () {
      var id = this.route.snapshot.paramMap.get("id");
      if (!id) {
        throw Error("keyresult id is undefined");
      }
      return parseInt(id);
    };
    KeyresultDetailComponent_1.prototype.loadKeyResult = function (id) {
      var _this = this;
      this.keyResultService
        .getFullKeyResult(id)
        .pipe(
          (0, rxjs_1.catchError)(function () {
            return rxjs_1.EMPTY;
          }),
        )
        .subscribe(function (keyResult) {
          _this.keyResult$.next(keyResult);
          var state = keyResult.objective.state;
          _this.isComplete = state === "SUCCESSFUL" || state === "NOTSUCCESSFUL";
        });
    };
    KeyresultDetailComponent_1.prototype.castToMetric = function (keyResult) {
      return keyResult;
    };
    KeyresultDetailComponent_1.prototype.castToOrdinal = function (keyResult) {
      return keyResult;
    };
    KeyresultDetailComponent_1.prototype.checkInHistory = function () {
      var _this = this;
      var dialogConfig = constantLibary_1.OKR_DIALOG_CONFIG;
      var dialogRef = this.dialog.open(check_in_history_dialog_component_1.CheckInHistoryDialogComponent, {
        data: {
          keyResult: this.keyResult$.getValue(),
          isComplete: this.isComplete,
        },
        width: dialogConfig.width,
        height: dialogConfig.height,
        maxHeight: dialogConfig.maxHeight,
        maxWidth: dialogConfig.maxWidth,
      });
      dialogRef.afterClosed().subscribe(function () {
        _this.refreshDataService.markDataRefresh();
      });
    };
    KeyresultDetailComponent_1.prototype.openEditKeyResultDialog = function (keyResult) {
      var _this = this;
      var dialogConfig = (0, common_1.isMobileDevice)()
        ? {
            maxWidth: "100vw",
            maxHeight: "100vh",
            height: "100vh",
            width: "100vw",
          }
        : {
            width: "45em",
            height: "auto",
          };
      this.dialog
        .open(keyresult_dialog_component_1.KeyresultDialogComponent, {
          height: dialogConfig.height,
          width: dialogConfig.width,
          maxHeight: dialogConfig.maxHeight,
          maxWidth: dialogConfig.maxWidth,
          data: {
            objective: keyResult.objective,
            keyResult: keyResult,
          },
        })
        .afterClosed()
        .subscribe(function (result) {
          if (
            (result === null || result === void 0 ? void 0 : result.closeState) === CloseState_1.CloseState.SAVED &&
            result.id
          ) {
            _this.loadKeyResult(result.id);
            _this.refreshDataService.markDataRefresh();
          } else if (
            (result === null || result === void 0 ? void 0 : result.closeState) === CloseState_1.CloseState.DELETED
          ) {
            _this.router.navigate([""]).then(function () {
              return _this.refreshDataService.markDataRefresh();
            });
          } else {
            _this.loadKeyResult(_this.keyResult$.getValue().id);
          }
        });
    };
    KeyresultDetailComponent_1.prototype.checkForDraftState = function (keyResult) {
      var _this = this;
      if (keyResult.objective.state.toUpperCase() === "DRAFT") {
        var dialogConfig = (0, common_1.isMobileDevice)()
          ? {
              maxWidth: "100vw",
              maxHeight: "100vh",
              height: "100vh",
              width: constantLibary_1.CONFIRM_DIALOG_WIDTH,
            }
          : {
              width: "45em",
              height: "auto",
            };
        this.dialog
          .open(confirm_dialog_component_1.ConfirmDialogComponent, {
            data: {
              draftCreate: true,
            },
            width: dialogConfig.width,
            height: dialogConfig.height,
            maxHeight: dialogConfig.maxHeight,
            maxWidth: dialogConfig.maxWidth,
          })
          .afterClosed()
          .subscribe(function (result) {
            if (result) {
              _this.openCheckInForm();
            }
          });
      } else {
        this.openCheckInForm();
      }
    };
    KeyresultDetailComponent_1.prototype.openCheckInForm = function () {
      var _this = this;
      var dialogConfig = (0, common_1.isMobileDevice)()
        ? {
            maxWidth: "100vw",
            maxHeight: "100vh",
            height: "100vh",
            width: "100vw",
          }
        : {
            width: "45em",
            height: "auto",
          };
      var dialogRef = this.dialog.open(check_in_form_component_1.CheckInFormComponent, {
        height: dialogConfig.height,
        width: dialogConfig.width,
        maxHeight: dialogConfig.maxHeight,
        maxWidth: dialogConfig.maxWidth,
        data: {
          keyResult: this.keyResult$.getValue(),
        },
      });
      dialogRef.afterClosed().subscribe(function () {
        _this.refreshDataService.reloadKeyResultSubject.next();
        _this.refreshDataService.markDataRefresh();
      });
    };
    KeyresultDetailComponent_1.prototype.backToOverview = function () {
      this.router.navigate([""]);
    };
    return KeyresultDetailComponent_1;
  })());
  __setFunctionName(_classThis, "KeyresultDetailComponent");
  (function () {
    var _metadata = typeof Symbol === "function" && Symbol.metadata ? Object.create(null) : void 0;
    _keyResultId_decorators = [(0, core_1.Input)()];
    __esDecorate(
      null,
      null,
      _keyResultId_decorators,
      {
        kind: "field",
        name: "keyResultId",
        static: false,
        private: false,
        access: {
          has: function (obj) {
            return "keyResultId" in obj;
          },
          get: function (obj) {
            return obj.keyResultId;
          },
          set: function (obj, value) {
            obj.keyResultId = value;
          },
        },
        metadata: _metadata,
      },
      _keyResultId_initializers,
      _keyResultId_extraInitializers,
    );
    __esDecorate(
      null,
      (_classDescriptor = { value: _classThis }),
      _classDecorators,
      { kind: "class", name: _classThis.name, metadata: _metadata },
      null,
      _classExtraInitializers,
    );
    KeyresultDetailComponent = _classThis = _classDescriptor.value;
    if (_metadata)
      Object.defineProperty(_classThis, Symbol.metadata, {
        enumerable: true,
        configurable: true,
        writable: true,
        value: _metadata,
      });
    __runInitializers(_classThis, _classExtraInitializers);
  })();
  return (KeyresultDetailComponent = _classThis);
})();
exports.KeyresultDetailComponent = KeyresultDetailComponent;
