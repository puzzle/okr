"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var customization_service_1 = require("./customization.service");
var rxjs_1 = require("rxjs");
var CallRecorder = /** @class */ (function () {
    function CallRecorder() {
        this.calls = {};
    }
    CallRecorder.prototype.add = function (key, value) {
        if (!this.calls[key]) {
            this.calls[key] = [];
        }
        this.calls[key].push(value);
    };
    CallRecorder.prototype.getCallByIdx = function (key, index) {
        if (index === void 0) { index = 0; }
        return this.calls[key][index];
    };
    CallRecorder.prototype.getCallCount = function (key) {
        var _a, _b;
        return (_b = (_a = this.calls[key]) === null || _a === void 0 ? void 0 : _a.length) !== null && _b !== void 0 ? _b : 0;
    };
    CallRecorder.prototype.clear = function () {
        this.calls = {};
    };
    return CallRecorder;
}());
describe('CustomizationService', function () {
    var body = {
        activeProfile: 'test',
        issuer: 'some-issuer.com',
        clientId: 'my-client-id',
        title: 'title',
        favicon: 'favicon',
        logo: 'logo',
        triangles: 'triangles',
        backgroundLogo: 'backgroundLogo',
        helpSiteUrl: 'https://wiki.puzzle.ch/Puzzle/OKRs',
        customStyles: { cssVar1: 'foo' },
    };
    var service;
    var configServiceMock;
    var documentMock;
    var callRecorder = new CallRecorder();
    var configSubject;
    beforeEach(function () {
        configSubject = new rxjs_1.BehaviorSubject(body);
        configServiceMock = { config$: configSubject.asObservable() };
        callRecorder.clear();
        documentMock = {
            getElementById: function (id) {
                return {
                    setAttribute: function () {
                        callRecorder.add("".concat(id, "-setAttribute"), arguments);
                    },
                };
            },
            querySelector: function (selector) {
                return {
                    set innerHTML(value) {
                        callRecorder.add("".concat(selector, ".innerHTML"), arguments);
                    },
                    get style() {
                        return {
                            setProperty: function () {
                                callRecorder.add("".concat(selector, ".style.setProperty"), arguments);
                            },
                            removeProperty: function () {
                                callRecorder.add("".concat(selector, ".style.removeProperty"), arguments);
                            },
                        };
                    },
                };
            },
        };
        service = new customization_service_1.CustomizationService(configServiceMock, documentMock);
    });
    it('should call correct apis when config is ready', function () {
        var currentConfig = service.getCurrentConfig();
        expect(currentConfig === null || currentConfig === void 0 ? void 0 : currentConfig.title).toBe(body.title);
        expect(currentConfig === null || currentConfig === void 0 ? void 0 : currentConfig.logo).toBe(body.logo);
        expect(currentConfig === null || currentConfig === void 0 ? void 0 : currentConfig.favicon).toBe(body.favicon);
        expect(currentConfig === null || currentConfig === void 0 ? void 0 : currentConfig.triangles).toBe(body.triangles);
        expect(currentConfig === null || currentConfig === void 0 ? void 0 : currentConfig.backgroundLogo).toBe(body.backgroundLogo);
        expect(currentConfig === null || currentConfig === void 0 ? void 0 : currentConfig.helpSiteUrl).toBe(body.helpSiteUrl);
        expect(currentConfig === null || currentConfig === void 0 ? void 0 : currentConfig.customStyles['cssVar1']).toBe(body.customStyles['cssVar1']);
        expect(callRecorder.getCallCount('title.innerHTML')).toBe(1);
        expect(callRecorder.getCallCount('favicon-setAttribute')).toBe(1);
        expect(callRecorder.getCallCount('html.style.setProperty')).toBe(1);
        expect(callRecorder.getCallCount('html.style.removeProperty')).toBe(0);
        expect(callRecorder.getCallByIdx('title.innerHTML', 0)[0]).toBe('title');
        expect(callRecorder.getCallByIdx('favicon-setAttribute', 0)[0]).toBe('href');
        expect(callRecorder.getCallByIdx('favicon-setAttribute', 0)[1]).toBe('favicon');
        expect(callRecorder.getCallByIdx('html.style.setProperty', 0)[0]).toBe('--cssVar1');
        expect(callRecorder.getCallByIdx('html.style.setProperty', 0)[1]).toBe('foo');
    });
    it('should update if config changed afterwards', function () {
        var bodySecond = {
            activeProfile: 'test-second',
            issuer: 'some-issuer.com-second',
            clientId: 'my-client-id-second',
            title: 'title-second',
            favicon: 'favicon-second',
            logo: 'logo-second',
            triangles: 'triangles-second',
            backgroundLogo: 'backgroundLogo-second',
            helpSiteUrl: 'https://wiki.puzzle.ch/Puzzle/OKRs',
            customStyles: { cssVarNew: 'bar' },
        };
        configSubject.next(bodySecond);
        var currentConfig = service.getCurrentConfig();
        expect(currentConfig === null || currentConfig === void 0 ? void 0 : currentConfig.title).toBe(bodySecond.title);
        expect(currentConfig === null || currentConfig === void 0 ? void 0 : currentConfig.logo).toBe(bodySecond.logo);
        expect(currentConfig === null || currentConfig === void 0 ? void 0 : currentConfig.favicon).toBe(bodySecond.favicon);
        expect(currentConfig === null || currentConfig === void 0 ? void 0 : currentConfig.triangles).toBe(bodySecond.triangles);
        expect(currentConfig === null || currentConfig === void 0 ? void 0 : currentConfig.backgroundLogo).toBe(bodySecond.backgroundLogo);
        expect(currentConfig === null || currentConfig === void 0 ? void 0 : currentConfig.helpSiteUrl).toBe(bodySecond.helpSiteUrl);
        expect(currentConfig === null || currentConfig === void 0 ? void 0 : currentConfig.customStyles['cssVarNew']).toBe(bodySecond.customStyles['cssVarNew']);
        expect(currentConfig === null || currentConfig === void 0 ? void 0 : currentConfig.customStyles['cssVar1']).toBe(undefined);
        expect(callRecorder.getCallCount('title.innerHTML')).toBe(2);
        expect(callRecorder.getCallCount('favicon-setAttribute')).toBe(2);
        expect(callRecorder.getCallCount('html.style.setProperty')).toBe(2);
        expect(callRecorder.getCallCount('html.style.removeProperty')).toBe(1);
        expect(callRecorder.getCallByIdx('title.innerHTML', 1)[0]).toBe('title-second');
        expect(callRecorder.getCallByIdx('favicon-setAttribute', 1)[0]).toBe('href');
        expect(callRecorder.getCallByIdx('favicon-setAttribute', 1)[1]).toBe('favicon-second');
        expect(callRecorder.getCallByIdx('html.style.setProperty', 1)[0]).toBe('--cssVarNew');
        expect(callRecorder.getCallByIdx('html.style.setProperty', 1)[1]).toBe('bar');
    });
});
