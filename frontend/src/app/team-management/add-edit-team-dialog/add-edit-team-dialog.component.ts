import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { formInputCheck, hasFormFieldErrors } from '../../shared/common';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { TeamService } from '../../services/team.service';
import { Team } from '../../shared/types/model/Team';
import { TeamMin } from '../../shared/types/model/TeamMin';
import { TranslateService } from '@ngx-translate/core';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';

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
    private teamService: TeamService,
    private userService: UserService,
    private router: Router,
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
      this.createNewTeam();
    } else {
      this.updateTeam();
    }
  }

  private createNewTeam() {
    let newTeam: Team = this.teamForm.value as Team;
    this.teamService.createTeam(newTeam).subscribe((result) => {
      this.userService.reloadUsers();
      this.userService.reloadCurrentUser().subscribe();
      this.dialogRef.close(result);
      this.router.navigateByUrl('/team-management/' + result.id);
    });
  }

  private updateTeam() {
    let updatedTeam: Team = {
      ...this.teamForm.value,
      id: this.data.team.id,
      version: this.data.team.version,
    } as Team;
    this.teamService.updateTeam(updatedTeam).subscribe((result) => {
      this.dialogRef.close(result);
    });
  }

  getErrorMessage(error: string, field: string, firstNumber: number | null, secondNumber: number | null): string {
    return field + this.translate.instant('DIALOG_ERRORS.' + error).format(firstNumber, secondNumber);
  }
}
