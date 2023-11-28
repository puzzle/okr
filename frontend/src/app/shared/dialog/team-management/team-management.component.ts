import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { formInputCheck, isMobileDevice } from '../../common';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import errorMessages from '../../../../assets/errors/error-messages.json';
import { Organisation } from '../../types/model/Organisation';
import { OrganisationService } from '../../services/organisation.service';
import { Observable, of } from 'rxjs';
import { TeamService } from '../../services/team.service';
import { Team } from '../../types/model/Team';
import { TeamMin } from '../../types/model/TeamMin';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { OrganisationState } from '../../types/enums/OrganisationState';
import { CONFIRM_DIALOG_WIDTH } from '../../constantLibary';
import { CloseState } from '../../types/enums/CloseState';

@Component({
  selector: 'app-team-management',
  templateUrl: './team-management.component.html',
  styleUrls: ['./team-management.component.scss'],
})
export class TeamManagementComponent implements OnInit {
  teamForm = new FormGroup({
    name: new FormControl<string>('', [Validators.required, Validators.minLength(2), Validators.maxLength(250)]),
    organisations: new FormControl<Organisation[]>([], [Validators.required]),
  });
  organisations$: Observable<Organisation[]> = new Observable<Organisation[]>();
  public hasInActiveOrganisations: boolean = false;
  protected readonly formInputCheck = formInputCheck;
  protected readonly errorMessages: any = errorMessages;

  constructor(
    public dialogRef: MatDialogRef<TeamManagementComponent>,
    private dialog: MatDialog,
    private organisationService: OrganisationService,
    private teamService: TeamService,
    @Inject(MAT_DIALOG_DATA)
    public data: {
      team: TeamMin;
    },
  ) {}

  ngOnInit(): void {
    this.organisations$ = this.organisationService.getOrganisations();
    if (this.data) {
      this.organisationService
        .getOrganisationsByTeamId(this.data.team.id)
        .subscribe((organisationsOfTeam: Organisation[]) => {
          this.mergeOrganisations(organisationsOfTeam);
          this.teamForm.setValue({
            name: this.data.team.name,
            organisations: organisationsOfTeam,
          });
          this.hasInActiveOrganisations =
            organisationsOfTeam.filter((organisation) => organisation.state != OrganisationState.ACTIVE).length > 0;
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

  mergeOrganisations(organisationsOfTeam: Organisation[]) {
    this.organisations$.subscribe((activeOrganisations) => {
      organisationsOfTeam
        .filter((organisation) => organisation.state == OrganisationState.INACTIVE)
        .forEach((organisation) => activeOrganisations.push(organisation));
      this.organisations$ = of(activeOrganisations);
    });
  }

  checkIfInActiveAdded() {
    this.hasInActiveOrganisations =
      this.teamForm.controls.organisations.value!.filter(
        (organisation) => organisation.state != OrganisationState.ACTIVE,
      ).length > 0;
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

  getMatOptionStyle(organisation: Organisation) {
    let isInActive = organisation.state == OrganisationState.INACTIVE;
    return { 'text-decoration': isInActive ? 'line-through' : 'none' };
  }

  isTouchedOrDirty(name: string) {
    return this.teamForm.get(name)?.dirty || this.teamForm.get(name)?.touched;
  }

  getErrorKeysOfFormField(name: string) {
    const errors = this.teamForm.get(name)?.errors;
    return errors == null ? [] : Object.keys(errors);
  }

  compareWithFunc(a: Organisation, b: Organisation) {
    return a.orgName === b.orgName;
  }
}
