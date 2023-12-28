import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { ActivatedRoute } from '@angular/router';
import { combineLatest, map, ReplaySubject, Subscription } from 'rxjs';
import { User } from '../../shared/types/model/User';
import { convertFromUsers, UserTableEntry } from '../../shared/types/model/UserTableEntry';
import { TeamService } from '../../services/team.service';
import { Team } from '../../shared/types/model/Team';

@Component({
  selector: 'app-member-list',
  templateUrl: './member-list.component.html',
  styleUrl: './member-list.component.scss',
})
export class MemberListComponent implements OnInit, OnDestroy {
  dataSource: UserTableEntry[] = [];
  selectedTeam: Team | undefined;

  private allUsersSubj: ReplaySubject<User[]> = new ReplaySubject<User[]>(1);

  private subscription!: Subscription;
  private allColumns = ['icon', 'name', 'roles', 'teams'];
  private teamColumns = ['icon', 'name', 'roles'];

  displayedColumns: string[] = this.allColumns;

  public constructor(
    private readonly userService: UserService,
    private readonly route: ActivatedRoute,
    private readonly cd: ChangeDetectorRef,
    private readonly teamService: TeamService,
  ) {}

  public ngOnInit(): void {
    this.userService.getUsers().subscribe((users) => this.allUsersSubj.next(users));
    const teamId$ = this.route.paramMap.pipe(map((params) => params.get('teamId')));
    this.subscription = combineLatest([
      this.allUsersSubj.asObservable(),
      teamId$,
      this.teamService.getAllTeams(),
    ]).subscribe(([users, teamIdParam, teams]) => {
      this.setDataSource(users, teamIdParam);
      this.setSelectedTeam(teams, teamIdParam);
    });
  }

  private setDataSource(users: User[], teamIdParam: string | null) {
    if (!teamIdParam) {
      this.dataSource = convertFromUsers(users, null);
      this.displayedColumns = this.allColumns;
      this.cd.markForCheck();
      return;
    }
    const teamId = parseInt(teamIdParam);
    this.dataSource = convertFromUsers(users, teamId);
    this.displayedColumns = this.teamColumns;
    this.cd.markForCheck();
  }

  public ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  private setSelectedTeam(teams: Team[], teamIdParam: string | null) {
    if (!teamIdParam) {
      this.selectedTeam = undefined;
      return;
    }
    this.selectedTeam = teams.find((t) => t.id === parseInt(teamIdParam));
    this.cd.markForCheck();
  }
}
