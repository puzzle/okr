import { Component, OnInit } from '@angular/core';
import { Observable, switchMap } from 'rxjs';
import {
  KeyResultService,
  Measure,
} from '../../../services/key-result.service';
import { getNumberOrNull } from '../../../common';
import { ActivatedRoute } from '@angular/router';
import { DatePipe } from '@angular/common';
import { ConfirmDialogComponent } from '../../../dialog/confirm-dialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { MeasureService } from '../../../services/measure.service';

@Component({
  selector: 'app-measure-row',
  templateUrl: './measure-row.component.html',
  styleUrls: ['./measure-row.component.scss'],
})
export class MeasureRowComponent implements OnInit {
  measures$!: Observable<Measure[]>;

  constructor(
    private keyresultService: KeyResultService,
    private route: ActivatedRoute,
    private datePipe: DatePipe,
    private dialog: MatDialog,
    private measureService: MeasureService
  ) {}

  ngOnInit(): void {
    this.measures$ = this.route.paramMap.pipe(
      switchMap((params) => {
        const keyResultId = getNumberOrNull(params.get('keyresultId'));
        if (keyResultId) {
          return this.keyresultService.getMeasuresOfKeyResult(keyResultId);
        } else {
          throw Error('KeyResult with Id ' + keyResultId + " doesn't exist");
        }
      })
    );
  }

  formatDate(date: string) {
    let convertedDate: Date = new Date(date);
    return this.datePipe.transform(convertedDate, 'dd.MM.yyyy', 'CEST');
  }

  delete() {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      disableClose: true,
      data: {
        title:
          'Willst du dieses Key Result und die dazugehörigen Messungen wirklich löschen?',
        confirmText: 'Bestätigen',
        closeText: 'Abbrechen',
      },
    });
    dialogRef.componentInstance.closeDialog.subscribe((confirm) => {
      if (confirm) {
        dialogRef.componentInstance.displaySpinner = true;
        //implement delete + toaster
      }
    });
  }
}
