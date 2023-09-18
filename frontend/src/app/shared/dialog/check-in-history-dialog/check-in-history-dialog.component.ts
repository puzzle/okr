import { Component, OnInit } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { CheckInMin } from '../../types/model/CheckInMin';
import { CheckInService } from '../../services/check-in.service';

@Component({
  selector: 'app-check-in-history-dialog',
  templateUrl: './check-in-history-dialog.component.html',
  styleUrls: ['./check-in-history-dialog.component.scss'],
})
export class CheckInHistoryDialogComponent implements OnInit {
  checkInHistory: Observable<CheckInMin[]> = new Subject<CheckInMin[]>();

  constructor(private chckinService: CheckInService) {}
  ngOnInit(): void {}
}
