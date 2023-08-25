import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { ObjectiveColumnComponent } from './objectiveColumn/objective-column.component';

@NgModule({
  declarations: [ObjectiveColumnComponent],
  imports: [CommonModule, MatCardModule, MatMenuModule, MatButtonModule],
})
export class ObjectiveModule {}
