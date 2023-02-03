import { Component, Input, OnInit } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
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
import { HttpErrorResponse } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { RouteService } from '../../../services/route.service';

@Component({
  selector: 'app-measure-row',
  templateUrl: './measure-row.component.html',
  styleUrls: ['./measure-row.component.scss'],
})
export class MeasureRowComponent implements OnInit {
  measures$: Subject<Measure[]> = new BehaviorSubject<Measure[]>([]);
  @Input() open: boolean = false;
  isMeasureForm!: boolean;
  keyResultId!: number | null;

  constructor(
    private keyresultService: KeyResultService,
    private route: ActivatedRoute,
    private datePipe: DatePipe,
    private dialog: MatDialog,
    private measureService: MeasureService,
    private toastr: ToastrService,
    public routeService: RouteService
  ) {}

  ngOnInit(): void {
    window.location.href.toString().includes('measure/')
      ? (this.isMeasureForm = true)
      : (this.isMeasureForm = false);

    this.route.paramMap.subscribe((params) => {
      const keyresultId = getNumberOrNull(params.get('keyresultId'));
      if (keyresultId) {
        this.keyResultId = keyresultId;
      } else {
        this.keyResultId = null;
      }
    });
    this.reloadMeasures();
  }

  formatDate(date: string) {
    let convertedDate: Date = new Date(date);
    return this.datePipe.transform(convertedDate, 'dd.MM.yyyy', 'CEST');
  }

  openDeleteDialog(id: number) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      disableClose: true,
      data: {
        title: 'Willst du diese Messung wirklich löschen?',
        confirmText: 'Bestätigen',
        closeText: 'Abbrechen',
      },
    });
    dialogRef.componentInstance.closeDialog.subscribe((confirm) => {
      if (confirm) {
        dialogRef.componentInstance.displaySpinner = true;
        this.measureService.deleteMeasureById(id).subscribe({
          next: () => {
            dialogRef.componentInstance.displaySpinner = false;
            dialogRef.close();
            this.reloadMeasures();
            this.toastr.success('', 'Messung gelöscht!', {
              timeOut: 5000,
            });
          },
          error: (e: HttpErrorResponse) => {
            dialogRef.componentInstance.displaySpinner = false;
            dialogRef.close();
            this.toastr.error(
              'Messung konnte nicht gelöscht werden!',
              'Fehlerstatus: ' + e.status,
              {
                timeOut: 5000,
              }
            );
          },
        });
      } else {
        dialogRef.close();
      }
    });
  }

  reloadMeasures(): void {
    this.route.paramMap.subscribe((params) => {
      const keyResultId = getNumberOrNull(params.get('keyresultId'));
      if (keyResultId) {
        this.keyresultService
          .getMeasuresOfKeyResult(keyResultId)
          .subscribe((data) => {
            this.measures$.next(data);
          });
      } else {
        throw Error('Key Result with Id ' + keyResultId + " doesn't exist");
      }
    });
  }
}
