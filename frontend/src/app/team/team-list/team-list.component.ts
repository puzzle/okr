import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { Team, TeamService } from '../../shared/services/team.service';
import { RouteService } from '../../shared/services/route.service';
import { ConfirmDialogComponent } from '../../shared/dialog/confirm-dialog/confirm-dialog.component';
import { HttpErrorResponse } from '@angular/common/http';
import { MatDialog } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-team-list',
  templateUrl: './team-list.component.html',
  styleUrls: ['./team-list.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TeamListComponent implements OnInit {
  @Input() team!: Team;
  @Output() onTeamsListUpdate: EventEmitter<any> = new EventEmitter();
  teamList$: Subject<Team[]> = new BehaviorSubject<Team[]>([]);

  constructor(
    private teamService: TeamService,
    public routeService: RouteService,
    public dialog: MatDialog,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.reloadTeams();
  }

  openDeleteDialog(id: number) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      disableClose: true,
      data: {
        title: 'Willst du dieses Team und die zugehörigen Objectives, Keyresults und Messungen wirklich löschen?',
        confirmText: 'Bestätigen',
        closeText: 'Abbrechen',
      },
    });
    dialogRef.componentInstance.closeDialog.subscribe((confirm) => {
      if (confirm) {
        dialogRef.componentInstance.displaySpinner = true;
        this.teamService.deleteTeamById(id).subscribe({
          next: () => {
            dialogRef.componentInstance.displaySpinner = false;
            dialogRef.close();
            this.reloadTeams();
            this.toastr.success('', 'Team gelöscht!', {
              timeOut: 5000,
            });
          },
          error: (e: HttpErrorResponse) => {
            dialogRef.componentInstance.displaySpinner = false;
            dialogRef.close();
            this.toastr.error('Team konnte nicht gelöscht werden!', 'Fehlerstatus: ' + e.status, {
              timeOut: 5000,
            });
          },
        });
      } else {
        dialogRef.close();
      }
    });
  }

  reloadTeams(): void {
    this.teamService.getTeams().subscribe((data) => {
      this.teamList$.next(data);
    });
  }
}
