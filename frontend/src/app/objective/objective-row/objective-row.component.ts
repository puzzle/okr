import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Objective, ObjectiveService } from '../../shared/services/objective.service';
import { MenuEntry } from '../../shared/types/menu-entry';
import { KeyResultMeasure, KeyResultService } from '../../shared/services/key-result.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { ConfirmDialogComponent } from '../../shared/dialog/confirm-dialog/confirm-dialog.component';
import { HttpErrorResponse } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { MatDialog } from '@angular/material/dialog';
import { getNumberOrNull } from '../../shared/common';
import { RouteService } from '../../shared/services/route.service';

@Component({
  selector: 'app-objective-row',
  templateUrl: './objective-row.component.html',
  styleUrls: ['./objective-row.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveRowComponent implements OnInit {
  @Input() objective!: Objective;
  @Output() onObjectivesListUpdate: EventEmitter<any> = new EventEmitter();
  keyResultList: Observable<KeyResultMeasure[]> = new BehaviorSubject([]);
  menuEntries!: MenuEntry[];
  isSelected: boolean = false;
  constructor(
    private keyResultService: KeyResultService,
    private objectiveService: ObjectiveService,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService,
    private dialog: MatDialog,
    public routeService: RouteService
  ) {}

  public getKeyResults(id: number) {
    this.keyResultList = this.keyResultService.getKeyResultsOfObjective(id);
    if (!this.isSelected) {
      this.isSelected = true;
      this.routeService.addToSelectedObjectives(id);
    }
  }
  ngOnInit(): void {
    this.menuEntries = [
      {
        displayName: 'Objective löschen',
        showDialog: true,
      },
    ];
    this.route.queryParams.subscribe((params) => {
      if (params['objectives'] !== undefined) {
        const selectedObjectives: string[] = params['objectives'].split(',');
        if (selectedObjectives.some((x) => getNumberOrNull(x) === this.objective.id)) {
          this.isSelected = true;
        }
      }
    });
  }

  removeObjectiveSelection(objectiveId: number) {
    this.routeService.removeFromSelectedObjectives(objectiveId);
    this.isSelected = false;
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
        title: 'Willst du dieses Objective und die dazugehörigen KeyResults und Messungen wirklich löschen?',
        confirmText: 'Bestätigen',
        closeText: 'Abbrechen',
      },
    });

    dialogRef.componentInstance.closeDialog.subscribe((confirm) => {
      if (confirm) {
        dialogRef.componentInstance.displaySpinner = true;
        this.objectiveService.deleteObjectiveById(this.objective.id!).subscribe({
          next: () => {
            dialogRef.componentInstance.displaySpinner = false;
            dialogRef.close();
            this.onObjectivesListUpdate.emit();
            this.toastr.success('', 'Objective gelöscht!', {
              timeOut: 5000,
            });
          },
          error: (e: HttpErrorResponse) => {
            dialogRef.componentInstance.displaySpinner = false;
            dialogRef.close();
            this.toastr.error('Objective konnte nicht gelöscht werden!', 'Fehlerstatus: ' + e.status, {
              timeOut: 5000,
            });
          },
        });
      } else {
        dialogRef.close();
      }
    });
  }

  removeKeyResultFromListAndReloadObjective(id: number) {
    this.keyResultList = this.keyResultService.getKeyResultsOfObjective(id);
    this.objectiveService.getObjectiveById(this.objective.id!).subscribe((objective) => {
      this.objective = objective;
    });
  }
}
