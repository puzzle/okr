import { PageObjectMapperBase } from './pageObjectMapperBase';

export default class AngularSearchBox extends PageObjectMapperBase {
  selector: string;

  constructor(selector: string) {
    super();
    this.selector = selector;
    this.validatePage();
  }

  fill(value: string) {
    const input = cy.get('input')
      .first();
    input.clear();
    input.type(value);
    return this;
  }

  shouldHaveOption(option: string) {
    cy.contains('.mat-mdc-autocomplete-panel mat-option', option);
    return this;
  }

  shouldHaveLabel(label: string) {
    cy.contains('.mat-mdc-autocomplete-panel .mat-mdc-optgroup-label', label);
    return this;
  }

  selectOption(option: string) {
    cy.contains('.mat-mdc-autocomplete-panel mat-option', option)
      .click();
  }

  getPage() {
    return cy.get(this.selector);
  }

  static from(selector: string): AngularSearchBox {
    return new this(selector);
  }

  validatePage(): void {
    this.getPage()
      .should('exist');
  }
}
