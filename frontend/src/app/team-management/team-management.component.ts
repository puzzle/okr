import { Component } from '@angular/core';
import { TeamService } from '../services/team.service';

@Component({
  selector: 'app-team-management',
  templateUrl: './team-management.component.html',
  styleUrl: './team-management.component.scss',
  standalone: false,
  providers: [TeamService]
})
export class TeamManagementComponent {}
