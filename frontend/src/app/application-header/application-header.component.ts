import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';

@Component({
  selector: 'app-application-header',
  templateUrl: './application-header.component.html',
  styleUrls: ['./application-header.component.scss']
})
export class ApplicationHeaderComponent implements OnInit {
  @ViewChild('okrTopbar')
  okrTopbar: ElementRef | undefined;
  @ViewChild('okrBanner')
  okrBanner: ElementRef | undefined;

  constructor() { }

  ngOnInit(): void {
  }

}
