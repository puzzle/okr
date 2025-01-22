import Dialog from './dialog';
import Chainable = Cypress.Chainable;

export default class ConfirmDialog extends Dialog {
  checkForContentOnDialog(text: string) {
    this.getPage()
      .contains(text)
      .should('exist');
    return this;
  }

  override submit() {
    cy.getByTestId('confirm-yes')
      .click();
  }

  override cancel() {
    cy.getByTestId('confirm-no')
      .click();
  }

  getPage(): Chainable {
    return cy.get('app-confirm-dialog');
  }
}
