import { Injectable } from '@angular/core';
import { NavigationExtras, Router } from '@angular/router';

@Injectable()
export class CustomRouter extends Router {
  constructor() {
    super();
  }

  override navigate(commands: any[], extras?: NavigationExtras | undefined): Promise<boolean> {
    const customExtras = { ...extras, queryParamsHandling: 'merge' } as NavigationExtras;
    return super.navigate(commands, customExtras);
  }
}
