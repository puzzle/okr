import Dialog from './dialog';
import ConfirmDialog from './confirmDialog';

export default class ObjectiveDialog extends Dialog {
  fillObjectiveTitle(title: string) {
    this.fillInputByTestId('title', title);
    return this;
  }

  fillObjectiveDescription(description: string) {
    this.fillInputByTestId('description', description);
    return this;
  }

  selectQuarter(quarter: string) {
    cy.get('select#quarter').select(quarter);
    return this;
  }

  toggleCreateKeyResults() {
    cy.getByTestId('keyResult-checkbox').find("[type='checkbox']").check();
    return this;
  }

  deleteObjective() {
    cy.getByTestId('delete').click();
    return new ConfirmDialog();
  }

  submitDraftObjective() {
    cy.getByTestId('safe-draft').click();
  }

  getPage(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('app-objective-form').should('exist');
  }
}
