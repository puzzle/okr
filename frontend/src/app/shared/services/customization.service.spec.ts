import { CustomizationService } from './customization.service';
import { ConfigService } from '../../config.service';
import { BehaviorSubject } from 'rxjs';
import { ClientConfig } from '../types/model/ClientConfig';

class CallRecorder {
  private calls: { [key: string]: any[] } = {};

  public add(key: string, value: any): void {
    if (!this.calls[key]) {
      this.calls[key] = [];
    }
    this.calls[key].push(value);
  }

  public getCallByIdx(key: string, index = 0): any[] {
    return this.calls[key][index];
  }

  public getCallCount(key: string): number {
    return this.calls[key]?.length ?? 0;
  }

  public clear(): void {
    this.calls = {};
  }
}

describe('CustomizationService', () => {
  const body: ClientConfig = {
    activeProfile: 'test',
    issuer: 'some-issuer.com',
    clientId: 'my-client-id',
    title: 'title',
    favicon: 'favicon',
    logo: 'logo',
    customStyles: { cssVar1: 'foo' },
  };

  let service: CustomizationService;
  let configServiceMock: ConfigService;
  let documentMock: Document;
  let callRecorder = new CallRecorder();
  let configSubject: BehaviorSubject<ClientConfig>;

  beforeEach(() => {
    configSubject = new BehaviorSubject<ClientConfig>(body);
    configServiceMock = { config$: configSubject.asObservable() } as ConfigService;
    callRecorder.clear();

    documentMock = {
      getElementById: (id: string) => {
        return {
          setAttribute: function () {
            callRecorder.add(`${id}-setAttribute`, arguments);
          },
        } as unknown as HTMLElement;
      },
      querySelector: (selector: string) => {
        return {
          set innerHTML(value: string) {
            callRecorder.add(`${selector}-innerHTML`, arguments);
          },
          get style() {
            return {
              setProperty: function () {
                callRecorder.add(`${selector}.style-setProperty`, arguments);
              },
              removeProperty: function () {
                callRecorder.add(`${selector}.style-removeProperty`, arguments);
              },
            };
          },
        };
      },
    } as unknown as Document;
    service = new CustomizationService(configServiceMock, documentMock);
  });

  it('should call correct apis when config is ready', () => {
    const currentConfig = service.getCurrentConfig();
    expect(currentConfig?.title).toBe(body.title);
    expect(currentConfig?.logo).toBe(body.logo);
    expect(currentConfig?.favicon).toBe(body.favicon);
    expect(currentConfig?.customStyles['cssVar1']).toBe(body.customStyles['cssVar1']);

    expect(callRecorder.getCallCount('title-innerHTML')).toBe(1);
    expect(callRecorder.getCallCount('favicon-setAttribute')).toBe(1);
    expect(callRecorder.getCallCount('html.style-setProperty')).toBe(1);
    expect(callRecorder.getCallCount('html.style-removeProperty')).toBe(0);

    expect(callRecorder.getCallByIdx('title-innerHTML', 0)[0]).toBe('title');
    expect(callRecorder.getCallByIdx('favicon-setAttribute', 0)[0]).toBe('href');
    expect(callRecorder.getCallByIdx('favicon-setAttribute', 0)[1]).toBe('favicon');
    expect(callRecorder.getCallByIdx('html.style-setProperty', 0)[0]).toBe('--cssVar1');
    expect(callRecorder.getCallByIdx('html.style-setProperty', 0)[1]).toBe('foo');
  });

  it('should update if config changed afterwards', () => {
    const bodySecond = {
      activeProfile: 'test-second',
      issuer: 'some-issuer.com-second',
      clientId: 'my-client-id-second',
      title: 'title-second',
      favicon: 'favicon-second',
      logo: 'logo-second',
      customStyles: { cssVarNew: 'bar' },
    };
    configSubject.next(bodySecond);

    const currentConfig = service.getCurrentConfig();
    expect(currentConfig?.title).toBe(bodySecond.title);
    expect(currentConfig?.logo).toBe(bodySecond.logo);
    expect(currentConfig?.favicon).toBe(bodySecond.favicon);
    expect(currentConfig?.customStyles['cssVarNew']).toBe(bodySecond.customStyles['cssVarNew']);
    expect(currentConfig?.customStyles['cssVar1']).toBe(undefined);

    expect(callRecorder.getCallCount('title-innerHTML')).toBe(2);
    expect(callRecorder.getCallCount('favicon-setAttribute')).toBe(2);
    expect(callRecorder.getCallCount('html.style-setProperty')).toBe(2);
    expect(callRecorder.getCallCount('html.style-removeProperty')).toBe(1);

    expect(callRecorder.getCallByIdx('title-innerHTML', 1)[0]).toBe('title-second');
    expect(callRecorder.getCallByIdx('favicon-setAttribute', 1)[0]).toBe('href');
    expect(callRecorder.getCallByIdx('favicon-setAttribute', 1)[1]).toBe('favicon-second');
    expect(callRecorder.getCallByIdx('html.style-setProperty', 1)[0]).toBe('--cssVarNew');
    expect(callRecorder.getCallByIdx('html.style-setProperty', 1)[1]).toBe('bar');
  });
});
