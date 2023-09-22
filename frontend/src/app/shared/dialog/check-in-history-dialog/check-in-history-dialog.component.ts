import { Component, Inject, OnInit } from '@angular/core';
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
  checkInHistory: CheckInMin[] = [];

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private checkInService: CheckInService,
  ) {}
  ngOnInit(): void {
    this.checkInService.getAllCheckInOfKeyResult(this.data.keyResultId).subscribe((result) => {
      this.checkInHistory = result;
    });
  }

  protected readonly errorMessages = errorMessages;
}
