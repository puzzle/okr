import Dialog from './dialog';

export default class CompleteDialog extends Dialog {
  override submit() {
    cy.getByTestId('submit')
      .click();
  }

  getPage(): Cypress.Chainable {
    return cy.get('app-complete-dialog');
  }
}
