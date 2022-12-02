import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KeyResultRowComponent } from './key-result-row/key-result-row.component';
import {MatExpansionModule} from "@angular/material/expansion";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatIconModule} from "@angular/material/icon";
import {MatMenuModule} from "@angular/material/menu";
import {KeyResultService} from "../services/key-result.service";
import {RouterLink} from "@angular/router";
import {MatButtonModule} from "@angular/material/button";



@NgModule({
  declarations: [
    KeyResultRowComponent
  ],
  providers: [
    KeyResultService
  ],
  exports: [
    KeyResultRowComponent
  ],
  imports: [
    CommonModule,
    MatExpansionModule,
    MatProgressBarModule,
    MatIconModule,
    MatMenuModule,
    RouterLink,
    MatButtonModule,
  ]
})
export class KeyresultModule { }
