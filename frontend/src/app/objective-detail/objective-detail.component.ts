import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChange,
  SimpleChanges,
} from '@angular/core';
import { Objective } from '../shared/types/model/Objective';
import { ObjectiveService } from '../shared/services/objective.service';
import { Router } from '@angular/router';
import { NotifierService } from '../shared/services/notifier.service';

@Component({
  selector: 'app-objective-detail',
  templateUrl: './objective-detail.component.html',
  styleUrls: ['./objective-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveDetailComponent implements OnChanges {
  objective!: Objective;
  @Input() objectiveId!: string;
  constructor(
    private objectiveService: ObjectiveService,
    private changeDetectorRef: ChangeDetectorRef,
    private router: Router,
    private notifierService: NotifierService,
  ) {}

  closeDrawer() {
    this.notifierService.closeDetailSubject.next();
  }

  ngOnChanges(changes: SimpleChanges) {
    this.objectiveService.getFullObjective(parseInt(this.objectiveId)).subscribe({
      next: (fullObjective) => {
        this.objective = fullObjective;
        this.changeDetectorRef.markForCheck();
      },
      error: (error) => {
        this.closeDrawer();
        console.error(error);
        this.router.navigate(['']);
      },
    });
  }
}
