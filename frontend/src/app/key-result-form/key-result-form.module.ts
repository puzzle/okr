import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { KeyResultFormRoutingModule } from './key-result-form-routing.module';
import { KeyResultFormComponent } from './key-result-form.component';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  declarations: [KeyResultFormComponent],
  imports: [CommonModule, KeyResultFormRoutingModule, MatIconModule],
})
export class KeyResultFormModule {}
