import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";

@Component({
  selector: 'app-team-role-dropdown',
  templateUrl: './team-role-dropdown.component.html',
  styleUrl: './team-role-dropdown.component.scss'
})
export class TeamRoleDropdownComponent implements OnInit {

  @Input({required: true})
  isAdmin!: boolean;

  @Output()
  isAdminChange = new EventEmitter<boolean>();

  adminControl!: FormControl<boolean | null>;

  ngOnInit(): void {
    this.adminControl = new FormControl(this.isAdmin, [
      Validators.required,
    ]);
  }

  public triggerIsAdminChange(): void {
    this.isAdminChange.next(this.isAdmin);
  }
}
