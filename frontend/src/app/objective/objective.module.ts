import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { ObjectiveColumnComponent } from './objectiveColumn/objective-column.component';

@NgModule({
  declarations: [ObjectiveColumnComponent],
  imports: [CommonModule, MatMenuModule, MatButtonModule],
  exports: [ObjectiveColumnComponent],
})
export class ObjectiveModule {}
