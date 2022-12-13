import { Component, OnInit } from '@angular/core';
import { KeyResultService } from '../../shared/services/key-result.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-keyresult-overview',
  templateUrl: './key-result-overview.component.html',
  styleUrls: ['./key-result-overview.component.scss'],
})
export class KeyResultOverviewComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
}
