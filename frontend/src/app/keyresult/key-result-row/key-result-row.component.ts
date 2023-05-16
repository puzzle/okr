import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MenuEntry } from '../../shared/types/menu-entry';
import { KeyResultMeasure, KeyResultService } from '../../shared/services/key-result.service';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../shared/dialog/confirm-dialog/confirm-dialog.component';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';
import { RouteService } from '../../shared/services/route.service';
import { getNumberOrNull } from '../../shared/common';

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

  // kept in because this will be needed later on when keyresult-menu gets more functionality
  // menuEntries!: MenuEntry[];

  menuEntry!: MenuEntry;
  isSelected: boolean = false;
  constructor(
    private datePipe: DatePipe,
    private router: Router,
    private keyResultService: KeyResultService,
    private toastr: ToastrService,
    private dialog: MatDialog,
    private routeService: RouteService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // kept in will be needed later on when keyresult-menu gets more functionality
    // this.menuEntries = [{ displayName: 'Key Result löschen', showDialog: true }];

    this.menuEntry = { displayName: 'Keyresult löschen', showDialog: true };
    this.route.queryParams.subscribe((params) => {
      if (params['keyresults'] !== undefined) {
        const selectedKeyResults: string[] = params['keyresults'].split(',');
        if (selectedKeyResults.some((x) => getNumberOrNull(x) === this.keyResult.id)) {
          this.isSelected = true;
        }
      }
    });
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
      this.routeService.navigate(menuEntry.routeLine!);
    }
  }

  openDialog() {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      disableClose: true,
      data: {
        title: 'Willst du dieses Key Result und die dazugehörigen Messungen wirklich löschen?',
        confirmText: 'Bestätigen',
        closeText: 'Abbrechen',
      },
    });

    dialogRef.componentInstance.closeDialog.subscribe((confirm) => {
      if (confirm) {
        dialogRef.componentInstance.displaySpinner = true;
        this.keyResultService.deleteKeyResultById(this.keyResult.id!).subscribe({
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
            this.toastr.error('Key Result konnte nicht gelöscht werden!', 'Fehlerstatus: ' + e.status, {
              timeOut: 5000,
            });
          },
        });
      } else {
        dialogRef.close();
      }
    });
  }

  removeKeyResultSelection(keyResultId: number) {
    this.routeService.removeFromSelectedKeyresult(keyResultId);
    this.isSelected = false;
  }

  addKeyResultSelection(keyResultId: number) {
    if (!this.isSelected) {
      this.isSelected = true;
      this.routeService.addToSelectedKeyresults(keyResultId);
    }
  }
}
