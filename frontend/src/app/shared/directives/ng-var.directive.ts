import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';

/**
 * A directive *ngVar like *ngIf for set a variable in the template.
 * Usage:
 *
 * <div *ngVar="false as variable">
 *       <span>{{variable | json}}</span>
 * </div>
 *
 * or
 *
 * <div *ngVar="{ x: 4 } as variable">
 *     <span>{{variable | json}}</span>
 * </div>
 */
@Directive({ selector: '[ngVar]' })
export class VarDirective {
  @Input()
  set ngVar(context: any) {
    this.context.$implicit = this.context.ngVar = context;
    this.updateView();
  }

  context: any = {};

  constructor(
    private vcRef: ViewContainerRef,
    private templateRef: TemplateRef<any>,
  ) {}

  updateView() {
    this.vcRef.clear();
    this.vcRef.createEmbeddedView(this.templateRef, this.context);
  }
}
