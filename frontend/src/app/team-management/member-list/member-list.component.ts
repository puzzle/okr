import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { ActivatedRoute } from '@angular/router';
import { combineLatest, map, ReplaySubject, Subscription } from 'rxjs';
import { User } from '../../shared/types/model/User';
import { convertFromUsers, UserTableEntry } from '../../shared/types/model/UserTableEntry';

@Component({
  selector: 'app-member-list',
  templateUrl: './member-list.component.html',
  styleUrl: './member-list.component.scss',
})
export class MemberListComponent implements OnInit, OnDestroy {
  dataSource: UserTableEntry[] = [];
  private allUsersSubj: ReplaySubject<User[]> = new ReplaySubject<User[]>(1);

  private subscription!: Subscription;
  private allColumns = ['name', 'roles', 'teams'];
  private teamColumns = ['name', 'roles'];

  displayedColumns: string[] = this.allColumns;

  public constructor(
    private readonly userService: UserService,
    private readonly route: ActivatedRoute,
    private readonly cd: ChangeDetectorRef,
  ) {}

  public ngOnInit(): void {
    this.userService.getUsers().subscribe((users) => this.allUsersSubj.next(users));
    const teamId$ = this.route.paramMap.pipe(map((params) => params.get('teamId')));
    this.subscription = combineLatest([this.allUsersSubj.asObservable(), teamId$]).subscribe(([users, teamIdParam]) =>
      this.setDataSource(users, teamIdParam),
    );
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
}
