import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ObjectiveColumnComponent } from './objective-column/objective-column.component';
import { MatCardModule } from '@angular/material/card';

@NgModule({
  declarations: [ObjectiveColumnComponent],
  imports: [CommonModule, MatCardModule],
})
export class ObjectiveModule {}
