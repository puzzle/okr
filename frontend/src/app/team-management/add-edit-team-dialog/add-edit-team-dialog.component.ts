import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { formInputCheck, hasFormFieldErrors, isMobileDevice } from '../../shared/common';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { TeamService } from '../../services/team.service';
import { Team } from '../../shared/types/model/Team';
import { TeamMin } from '../../shared/types/model/TeamMin';
import { ConfirmDialogComponent } from '../../shared/dialog/confirm-dialog/confirm-dialog.component';
import { CONFIRM_DIALOG_WIDTH } from '../../shared/constantLibary';
import { CloseState } from '../../shared/types/enums/CloseState';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-add-edit-team-dialog',
  templateUrl: './add-edit-team-dialog.component.html',
  styleUrls: ['./add-edit-team-dialog.component.scss'],
})
export class AddEditTeamDialog implements OnInit {
  teamForm = new FormGroup({
    name: new FormControl<string>('', [Validators.required, Validators.minLength(2), Validators.maxLength(250)]),
  });
  public hasInActiveOrganisations: boolean = false;
  protected readonly formInputCheck = formInputCheck;
  protected readonly hasFormFieldErrors = hasFormFieldErrors;

  constructor(
    public dialogRef: MatDialogRef<AddEditTeamDialog>,
    private dialog: MatDialog,
    private teamService: TeamService,
    @Inject(MAT_DIALOG_DATA)
    public data: {
      team: TeamMin;
    },
    private translate: TranslateService,
  ) {}

  ngOnInit(): void {
    if (this.data) {
      this.teamForm.setValue({
        name: this.data.team.name,
      });
    }
  }

  saveTeam() {
    if (!this.data) {
      let newTeam: Team = this.teamForm.value as Team;
      this.teamService.createTeam(newTeam).subscribe((result) => {
        this.dialogRef.close(result);
      });
    } else {
      let updatedTeam: Team = {
        ...this.teamForm.value,
        id: this.data.team.id,
        version: this.data.team.version,
      } as Team;
      this.teamService.updateTeam(updatedTeam).subscribe((result) => {
        this.dialogRef.close(result);
      });
    }
  }

  deleteTeam() {
    const dialogConfig = isMobileDevice()
      ? {
          maxWidth: '100vw',
          maxHeight: '100vh',
          height: '100vh',
          width: '100vw',
        }
      : {
          width: CONFIRM_DIALOG_WIDTH,
          height: 'auto',
        };
    const dialog = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Team',
      },
      width: dialogConfig.width,
      height: dialogConfig.height,
      maxHeight: dialogConfig.maxHeight,
      maxWidth: dialogConfig.maxWidth,
    });
    dialog.afterClosed().subscribe((result) => {
      if (result) {
        this.teamService.deleteTeam(this.data.team.id).subscribe(() => {
          this.dialogRef.close({ state: CloseState.DELETED, id: this.data.team.id });
        });
      }
    });
  }

  getErrorMessage(error: string, field: string, firstNumber: number | null, secondNumber: number | null): string {
    return field + this.translate.instant('DIALOG_ERRORS.' + error).format(firstNumber, secondNumber);
  }
}
