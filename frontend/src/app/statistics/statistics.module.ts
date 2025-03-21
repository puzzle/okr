import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StatisticsRoutingModule } from './statistics-routing.module';
import { StatisticsComponent } from './statistics.component';
import { SharedModule } from '../shared/shared.module';
import { StatisticsCardComponent } from './statistics-card/statistics-card.component';
import { StatisticsBarComponent } from './statistics-bar/statistics-bar.component';
import { StatisticsInformationComponent } from './statistics-information/statistics-information.component';
import { MatProgressBar } from '@angular/material/progress-bar';


@NgModule({
  declarations: [
    StatisticsComponent,
    StatisticsCardComponent,
    StatisticsBarComponent,
    StatisticsInformationComponent
  ],
  imports: [
    CommonModule,
    StatisticsRoutingModule,
    SharedModule,
    MatProgressBar
  ]
})
export class StatisticsModule { }
