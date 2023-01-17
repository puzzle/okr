import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
} from '@angular/core';
import { MenuEntry } from '../../shared/types/menu-entry';
import {
  KeyResultMeasure,
  KeyResultService,
} from '../../shared/services/key-result.service';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../shared/dialog/confirm-dialog/confirm-dialog.component';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-keyresult-row',
  templateUrl: './key-result-row.component.html',
  styleUrls: ['./key-result-row.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class KeyResultRowComponent implements OnInit {
  @Input() keyResult!: KeyResultMeasure;
  @Input() objectiveId!: number;
  @Output() onKeyresultListUpdate: EventEmitter<any> = new EventEmitter();
  menuEntries!: MenuEntry[];

  constructor(
    private datePipe: DatePipe,
    private router: Router,
    private keyResultService: KeyResultService,
    private toastr: ToastrService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.menuEntries = [
      {
        displayName: 'KeyResult bearbeiten',
        showDialog: false,
        routeLine:
          'objective/' +
          this.objectiveId +
          '/keyresult/edit/' +
          this.keyResult.id,
      },
      {
        displayName: 'KeyResult duplizieren',
        showDialog: false,
        routeLine: 'objective/edit',
      },
      {
        displayName: 'Details einsehen',
        showDialog: false,
        routeLine: 'keyresults/' + this.keyResult.id,
      },
      { displayName: 'KeyResult löschen', showDialog: true },
      {
        displayName: 'Messung hinzufügen',
        showDialog: false,
        routeLine: 'keyresults/' + this.keyResult.id + '/measure/new',
      },
    ];
  }

  public formatDate(): string {
    if (this.keyResult.measure?.measureDate == undefined) {
      return '-';
    }
    var convertedDate: Date = new Date(this.keyResult.measure?.measureDate!);
    const formattedDate = this.datePipe.transform(convertedDate, 'dd.MM.yyyy');
    if (formattedDate === null) {
      return '-';
    } else {
      return formattedDate;
    }
  }

  redirect(menuEntry: MenuEntry) {
    if (menuEntry.showDialog) {
      this.openDialog();
    } else {
      this.router.navigate([menuEntry.routeLine]);
    }
  }

  openDialog() {
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
        this.keyResultService
          .deleteKeyResultById(this.keyResult.id!)
          .subscribe({
            next: () => {
              dialogRef.componentInstance.displaySpinner = false;
              dialogRef.close();
              this.onKeyresultListUpdate.emit(this.keyResult.objectiveId!);
              this.toastr.success('', 'Key Result gelöscht!', {
                timeOut: 5000,
              });
            },
            error: (e: HttpErrorResponse) => {
              dialogRef.componentInstance.displaySpinner = false;
              dialogRef.close();
              this.toastr.error(
                'Key Result konnte nicht gelöscht werden!',
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
}
