import { PageObjectMapperBase } from '../pageObjectMapperBase';

export default abstract class Dialog extends PageObjectMapperBase {
  constructor() {
    super();
    this.validatePage();
  }

  override validatePage() {
    this.getPage().should('exist');
  }

  submit() {
    cy.getByTestId('safe').click();
  }

  cancel() {
    cy.getByTestId('cancel').click();
  }

  close() {
    cy.getByTestId('close-dialog').click();
  }

  protected fillInputByTestId(testId: string, value: string) {
    const elem = cy.getByTestId(testId);
    this.fillInput(elem, value);
  }

  protected fillInput(elem: Cypress.Chainable<JQuery<HTMLElement>>, value: string) {
    elem.clear();
    elem.type(value);
  }

  abstract getPage(): Cypress.Chainable<JQuery<HTMLElement>>;
}