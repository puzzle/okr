import { Component, OnDestroy, OnInit } from "@angular/core";
import { TeamService } from "../../services/team.service";
import { Observable, Subject, takeUntil } from "rxjs";
import { Team } from "../../shared/types/model/Team";
import { ActivatedRoute } from "@angular/router";

@Component({
  selector: "app-team-list",
  templateUrl: "./team-list.component.html",
  styleUrl: "./team-list.component.scss"
})
export class TeamListComponent implements OnInit, OnDestroy {
  public teams$: Observable<Team[]>;

  public selectedTeamId: number | undefined;

  private unsubscribe$ = new Subject<void>();

  constructor(private readonly teamService: TeamService,
    private readonly route: ActivatedRoute) {
    this.teams$ = teamService.getAllTeams();
  }

  public ngOnInit(): void {
    this.route.paramMap.pipe(takeUntil(this.unsubscribe$))
      .subscribe((params) => {
        const teamId = params.get("teamId");
        this.selectedTeamId = teamId ? parseInt(teamId) : undefined;
      });
  }

  public ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }
}
