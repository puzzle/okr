import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {TeamDetailComponent} from "./team-detail/team-detail.component";
import {TeamService} from "../services/team.service";
import {MatExpansionModule} from "@angular/material/expansion";
import {ObjectiveModule} from "../objective/objective.module";
import {KeyresultModule} from "../keyresult/keyresult.module";



@NgModule({
  declarations: [
    TeamDetailComponent
  ],
  providers: [
    TeamService
  ],
  exports: [
    TeamDetailComponent
  ],
  imports: [
    CommonModule,
    MatExpansionModule,
    ObjectiveModule,
  ]
})
export class TeamModule { }
