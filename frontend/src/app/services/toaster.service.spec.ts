import { TestBed } from '@angular/core/testing';

import { ToasterService } from './toaster.service';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { ToasterType } from '../shared/types/enums/toaster-type';

describe('ToasterService', () => {
  let service: ToasterService;
  let toaster: ToastrService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ToastrModule.forRoot()],
      providers: [ToasterService]
    });
    service = TestBed.inject(ToasterService);
    toaster = TestBed.inject(ToastrService);
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  it('should call right method for showSuccess', () => {
    jest.spyOn(toaster, 'success');
    service.showSuccess('test');
    expect(toaster.success)
      .toHaveBeenCalledWith('test', 'Erfolgreich!');
  });

  it('should call right method for showError', () => {
    jest.spyOn(toaster, 'error');
    service.showError('test');
    expect(toaster.error)
      .toHaveBeenCalledWith('test', 'Fehler!');
  });

  it('should call right method for showWarn', () => {
    jest.spyOn(toaster, 'warning');
    service.showWarn('test');
    expect(toaster.warning)
      .toHaveBeenCalledWith('test', 'Warnung!');
  });

  it.each([
    [ToasterType.SUCCESS,
      'message',
      'showSuccess'],
    [ToasterType.WARN,
      'message',
      'showWarn'],
    [ToasterType.ERROR,
      'message',
      'showError'],
    [999,
      'message',
      'showSuccess']
  ])('should call right method for dynamic toaster', (toasterType: number, message: string, func: any) => {
    const spy = jest.spyOn(service, func);

    service.showCustomToaster(message, toasterType);

    expect(spy)
      .toHaveBeenCalledWith(message);
  });
});
