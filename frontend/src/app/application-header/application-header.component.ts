import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-application-header',
  templateUrl: './application-header.component.html',
  styleUrls: ['./application-header.component.scss'],
})
export class ApplicationHeaderComponent implements OnInit {
  lastScrollTop: number = 0;

  constructor() {}

  ngOnInit(): void {
    window.addEventListener('scroll', this.changeHeaderAppearance);
  }

  changeHeaderAppearance() {
    let scrollTop: number = window.scrollY || document.documentElement.scrollTop;
    let navbar = document.getElementById('okrBanner');
    navbar!.style.top = scrollTop > this.lastScrollTop ? '-220px' : '48px';
    this.lastScrollTop = scrollTop;
  }
}
