import { TestBed } from '@angular/core/testing';

import { ToasterService } from './toaster.service';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { ToasterType } from '../types/enums/ToasterType';

describe('ToasterService', () => {
  let service: ToasterService;
  let toastr: ToastrService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ToastrModule.forRoot()],
      providers: [ToasterService],
    });
    service = TestBed.inject(ToasterService);
    toastr = TestBed.inject(ToastrService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('showSuccess should call right method', () => {
    jest.spyOn(toastr, 'success');
    service.showSuccess('test');
    expect(toastr.success).toBeCalledWith('test', 'Erfolgreich!');
  });

  it('showError should call right method', () => {
    jest.spyOn(toastr, 'error');
    service.showError('test');
    expect(toastr.error).toBeCalledWith('test', 'Fehler!');
  });

  it('showWarn should call right method', () => {
    jest.spyOn(toastr, 'warning');
    service.showWarn('test');
    expect(toastr.warning).toBeCalledWith('test', 'Warnung!');
  });

  it.each([
    [ToasterType.SUCCESS, 'message', 'showSuccess'],
    [ToasterType.WARN, 'message', 'showWarn'],
    [ToasterType.ERROR, 'message', 'showError'],
    [999, 'message', 'showSuccess'],
  ])('showWarn should call right method', (toasterType: number, message: string, func: any) => {
    const spy = jest.spyOn(service, func);

    service.showCustomToaster(message, toasterType);

    expect(spy).toBeCalledWith(message);
  });
});
