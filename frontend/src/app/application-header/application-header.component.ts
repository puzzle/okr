import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-application-header',
  templateUrl: './application-header.component.html',
  styleUrls: ['./application-header.component.scss'],
})
export class ApplicationHeaderComponent implements OnInit {
  @ViewChild('okrBanner')
  okrBanner: ElementRef | undefined;
  @ViewChild('okrTopbar')
  okrTopbar: ElementRef | undefined;
  lastScrollTop: number = 0;

  constructor() {}

  ngOnInit(): void {
    let navbar = document.getElementById('okrBanner');
    window.addEventListener('scroll', this.scrollFunction);
  }

  scrollFunction() {
    let scrollTop = window.scrollY || document.documentElement.scrollTop;
    let navbar = document.getElementById('okrBanner');
    if (navbar != null) {
      if (scrollTop > this.lastScrollTop) {
        navbar.style.top = '-220px';
      } else {
        navbar.style.top = '48px';
      }
    }
    this.lastScrollTop = scrollTop;
  }
}
