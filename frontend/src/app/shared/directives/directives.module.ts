import { NgModule } from '@angular/core';
import { VarDirective } from './ng-var.directive';

const directives = [VarDirective];

@NgModule({
  imports: [],
  declarations: [...directives],
  exports: [...directives],
})
export class DirectivesModule {}
