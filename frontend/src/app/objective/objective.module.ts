import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ObjectiveDetailComponent } from './objective-detail/objective-detail.component';
import { MatExpansionModule } from '@angular/material/expansion';
import { ObjectiveRowComponent } from './objective-row/objective-row.component';
import { ObjectiveService } from '../shared/services/objective.service';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { KeyresultModule } from '../keyresult/keyresult.module';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';

@NgModule({
  declarations: [ObjectiveDetailComponent, ObjectiveRowComponent],
  providers: [ObjectiveService],
  exports: [ObjectiveRowComponent, ObjectiveDetailComponent],
  imports: [
    CommonModule,
    MatExpansionModule,
    MatProgressBarModule,
    MatIconModule,
    MatMenuModule,
    KeyresultModule,
    MatButtonModule,
    RouterLink,
  ],
})
export class ObjectiveModule {}
