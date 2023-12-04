import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { ToasterType } from '../types/enums/ToasterType';

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

  showCustomToaster(msg: string, type?: ToasterType) {
    switch (type) {
      case ToasterType.SUCCESS:
        this.showSuccess(msg);
        break;
      case ToasterType.WARN:
        this.showWarn(msg);
        break;
      case ToasterType.ERROR:
        this.showError(msg);
        break;
      default:
        this.showSuccess(msg);
    }
  }
}
