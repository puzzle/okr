import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { KeyResultFormRoutingModule } from './key-result-form-routing.module';
import { KeyResultFormComponent } from './key-result-form.component';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [KeyResultFormComponent],
  imports: [
    CommonModule,
    KeyResultFormRoutingModule,
    MatIconModule,
    MatDividerModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
  ],
})
export class KeyResultFormModule {}
