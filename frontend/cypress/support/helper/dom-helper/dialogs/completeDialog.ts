import Dialog from './dialog';

export default class CompleteDialog extends Dialog {
  override submit() {
    cy.getByTestId('submit')
      .click();
  }

  completeAs(isSuccessful: boolean) {
    isSuccessful ? cy.getByTestId('successful')
      .click() : cy.getByTestId('not-successful')
      .click();
    return this;
  }

  writeClosingComment(comment: string) {
    cy.getByTestId('completeComment')
      .type(comment);
    return this;
  }

  getPage(): Cypress.Chainable {
    return cy.get('app-complete-dialog');
  }
}
