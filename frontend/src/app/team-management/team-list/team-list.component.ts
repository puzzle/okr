import {Component, OnDestroy, OnInit} from '@angular/core';
import {TeamService} from "../../services/team.service";
import {Observable, Subscription} from "rxjs";
import {Team} from "../../shared/types/model/Team";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-team-list',
  templateUrl: './team-list.component.html',
  styleUrl: './team-list.component.scss'
})
export class TeamListComponent implements OnInit, OnDestroy {

  public teams$: Observable<Team[]>;
  public selectedTeamId: number | undefined;
  private subscription!: Subscription;

  constructor(
    private readonly teamService: TeamService,
    private readonly route: ActivatedRoute
  ) {
    this.teams$ = teamService.getAllTeams();
  }

  public ngOnInit(): void {
    this.subscription = this.route.paramMap.subscribe(params => {
      const teamId = params.get('teamId');
      this.selectedTeamId = teamId ? parseInt(teamId) : undefined;
    });
  }

  public ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }


}
