import Dialog from './dialog';
import Chainable = Cypress.Chainable;

export default class ConfirmDialog extends Dialog {
  checkTitle(title: string) {
    this.getPage().contains(title).should('exist');
    return this;
  }

  checkDescription(title: string) {
    this.getPage().contains(title).should('exist');
    return this;
  }

  override submit() {
    cy.getByTestId('confirm-yes').click();
  }

  override cancel() {
    cy.getByTestId('confirm-no').click();
  }

  getPage(): Chainable {
    return cy.get('app-confirm-dialog');
  }
}
