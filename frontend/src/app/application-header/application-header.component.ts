import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-application-header',
  templateUrl: './application-header.component.html',
  styleUrls: ['./application-header.component.scss'],
})
export class ApplicationHeaderComponent implements OnInit {
  lastScrollTop: number = 0;
  @ViewChild('okrTopbar')
  okrTopbar: ElementRef | undefined;
  @ViewChild('okrBanner')
  okrBanner: ElementRef | undefined;

  constructor() {}

  ngOnInit(): void {
    let comp = document.getElementById('componentDiv');
    comp!.style.height = '208px';
    window.addEventListener('scroll', this.changeHeaderAppearance);
  }

  changeHeaderAppearance() {
    let scrollTop: number = window.scrollY || document.documentElement.scrollTop;
    let navbar = document.getElementById('okrBanner');
    let comp = document.getElementById('componentDiv');
    if (navbar != null) {
      comp!.style.height = scrollTop > this.lastScrollTop ? '48px' : '208px';

      navbar.style.top = scrollTop > this.lastScrollTop ? '-220px' : '48px';
    }
    this.lastScrollTop = scrollTop;
  }
}
