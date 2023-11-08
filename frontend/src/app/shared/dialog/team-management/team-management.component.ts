import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { formInputCheck } from '../../common';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import errorMessages from '../../../../assets/errors/error-messages.json';
import { Unit } from '../../types/enums/Unit';
import { Organisation } from '../../types/model/Organisation';
import { OrganisationService } from '../../services/organisation.service';
import { Observable } from 'rxjs';

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
  protected readonly formInputCheck = formInputCheck;
  protected readonly errorMessages: any = errorMessages;

  constructor(
    public dialogRef: MatDialogRef<TeamManagementComponent>,
    private dialog: MatDialog,
    private organisationService: OrganisationService,
  ) {}

  ngOnInit(): void {
    this.organisations$ = this.organisationService.getOrganisations();
  }

  saveTeam() {
    console.log(this.teamForm.value);
  }

  isTouchedOrDirty(name: string) {
    return this.teamForm.get(name)?.dirty || this.teamForm.get(name)?.touched;
  }

  getErrorKeysOfFormField(name: string) {
    const errors = this.teamForm.get(name)?.errors;
    return errors == null ? [] : Object.keys(errors);
  }
}
