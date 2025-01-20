import Dialog from './dialog';
import ConfirmDialog from './confirmDialog';
import Chainable = Cypress.Chainable;

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
    cy.get('select#quarter')
      .select(quarter);
    return this;
  }

  toggleCreateKeyResults() {
    cy.getByTestId('keyResult-checkbox')
      .find('[type=\'checkbox\']')
      .check();
    return this;
  }

  deleteObjective() {
    cy.getByTestId('delete')
      .click();
    return new ConfirmDialog();
  }

  submitDraftObjective() {
    cy.getByTestId('save-draft')
      .click();
  }

  excludeKeyResults(keyResults: string[]) {
    keyResults.forEach((keyResult) => {
      cy.get('label')
        .contains(keyResult.slice(0, 30))
        .click();
    });
    return this;
  }

  checkForPrimaryButton(submitButtonId: string) {
    cy.getByTestId(submitButtonId)
      .should('have.attr', 'color', 'primary')
      .and('have.attr', 'mat-flat-button');
    return this;
  }

  getPage(): Chainable {
    return cy.get('app-objective-form')
      .should('exist');
  }
}
