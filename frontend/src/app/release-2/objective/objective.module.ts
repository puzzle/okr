import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ObjectiveColumnComponent } from './objective-column/objective-column.component';
import { MatCardModule } from '@angular/material/card';
import { MatMenuModule } from '@angular/material/menu';

@NgModule({
  declarations: [ObjectiveColumnComponent],
  imports: [CommonModule, MatCardModule, MatMenuModule],
})
export class ObjectiveModule {}
