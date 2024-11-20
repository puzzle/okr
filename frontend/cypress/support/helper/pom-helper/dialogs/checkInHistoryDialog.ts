import Dialog from './dialog';
import CheckInDialog from './checkInDialog';

export default class CheckInHistoryDialog extends Dialog {
  override submit() {
    throw new Error('This dialog doesnt have a submit button');
  }

  override cancel() {
    cy.getByTestId('closeButton').click();
  }

  editLatestCheckIn() {
    cy.getByTestId('edit-check-in').first().click();
    return new CheckInDialog();
  }

  getPage(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('app-check-in-history-dialog');
  }
}
