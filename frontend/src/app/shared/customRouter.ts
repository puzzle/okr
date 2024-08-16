import { Injectable } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';

@Injectable()
export class CustomRouter extends Router {
  constructor() {
    super();
  }

  override navigate(commands: any[], extras?: NavigationExtras | undefined): Promise<boolean> {
    // const noMergeParams = ['iss', 'state', 'session_state'];
    // const newQueryParam = new URLSearchParams(window.location.search);
    // if (noMergeParams.every((e) => newQueryParam.has(e))) {
    //   return super.navigate(commands, extras);
    // }

    const customExtras = { ...extras, queryParamsHandling: 'merge' } as NavigationExtras;
    return super.navigate(commands, customExtras);
  }
}
