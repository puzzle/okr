import { Component, Inject, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { CheckInMin } from '../../types/model/CheckInMin';
import { CheckInService } from '../../services/check-in.service';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import errorMessages from '../../../../assets/errors/error-messages.json';

@Component({
  selector: 'app-check-in-history-dialog',
  templateUrl: './check-in-history-dialog.component.html',
  styleUrls: ['./check-in-history-dialog.component.scss'],
})
export class CheckInHistoryDialogComponent implements OnInit {
  checkInHistory: Observable<CheckInMin[]> = new Observable<CheckInMin[]>();

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private checkInService: CheckInService,
  ) {}
  ngOnInit(): void {
    this.checkInHistory = this.checkInService.getAllCheckInOfKeyResult(this.data.keyResultId);
  }

  protected readonly errorMessages = errorMessages;
}
