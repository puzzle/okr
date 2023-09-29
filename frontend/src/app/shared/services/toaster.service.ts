import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root',
})
export class ToasterService {
  constructor(private toastr: ToastrService) {}

  showSuccess(msg: string) {
    this.toastr.success(msg, 'Success');
  }

  showError(msg: string) {
    this.toastr.error(msg, 'Error');
  }

  showInfo() {
    this.toastr.info('Hello world!', 'Toastr fun!');
  }
}
