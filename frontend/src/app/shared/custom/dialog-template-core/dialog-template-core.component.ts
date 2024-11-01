import { Component, Input } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-dialog-template-core',
  templateUrl: './dialog-template-core.component.html',
  styleUrl: './dialog-template-core.component.scss',
})
export class DialogTemplateCoreComponent {
  @Input() observable: Observable<any> = new Observable();
  @Input() title: string = '';

  isValueReady(obj: any): boolean {
    if (obj == null) {
      return false;
    }
    if (Array.isArray(obj)) {
      return obj.length > 0;
    }
    return true;
  }
}
