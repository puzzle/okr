import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { TOASTER_TYPE } from '../constantLibary';

@Injectable({
  providedIn: 'root',
})
export class ToasterService {
  constructor(private toastr: ToastrService) {}

  showSuccess(msg: string) {
    this.toastr.success(msg, 'Erfolgreich!');
  }

  showError(msg: string) {
    this.toastr.error(msg, 'Fehler!');
  }

  showWarn(msg: string) {
    this.toastr.warning(msg, 'Warnung!');
  }

  showCustomToaster(msg: string, type: TOASTER_TYPE) {
    switch (type) {
      case 'SUCCESS':
        this.showSuccess(msg);
        break;
      case 'WARN':
        this.showWarn(msg);
        break;
      case 'ERROR':
        this.showError(msg);
        break;
      default:
        this.showSuccess(msg);
    }
  }
}
