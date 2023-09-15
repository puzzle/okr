import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Objective } from '../shared/types/model/Objective';

@Component({
  selector: 'app-objective-detail',
  templateUrl: './objective-detail.component.html',
  styleUrls: ['./objective-detail.component.scss'],
})
export class ObjectiveDetailComponent implements OnInit {
  @Input() objective!: Objective;
  @Output() close: EventEmitter<any> = new EventEmitter<any>();
  constructor() // private route: Route, // private router: Router,
  // private activatedRouteSnapshot: ActivatedRouteSnapshot
  {}

  closeDrawer() {
    this.close.emit();
  }
  ngOnInit(): void {
    // this.router.navigate(['/keyresult', keyresutId]);
    // const id: Observable<number> = this.route.params<number>('id');
    // this.route.params<number>('id').subscribe(id => {
    //   console.log(id);
    // });
    // this.activatedRouteSnapshot.paramMap.get('id');
    // const urlSegment: UrlSegment[] = this.activatedRouteSnapshot.url;
    // urlSegment[0].path; // -> objective
    // urlSegment[1].parameters['id'];
    //
    // // this.route.paramMap
    // this.route.queries['filter'];
  }
}
