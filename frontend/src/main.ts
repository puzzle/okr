import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';
import { isArray } from '@angular/compiler-cli/src/ngtsc/annotations/common';

if (environment.production) {
  enableProdMode();
}

declare global {
  interface String {
    format(): string;
  }
}

String.prototype.format = function () {
  const args = Array.from(arguments).flat();
  return this.replace(/{([0-9]+)}/g, function (match, index) {
    return typeof args[index] == 'undefined' ? match : args[index];
  });
};

platformBrowserDynamic()
  .bootstrapModule(AppModule)
  .catch((err) => console.error(err));
