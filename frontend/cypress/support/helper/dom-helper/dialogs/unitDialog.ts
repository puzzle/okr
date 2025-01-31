import Dialog from './dialog';
import Chainable = Cypress.Chainable;
import KeyResultDialog from './keyResultDialog';

export default class UnitDialog extends Dialog {
  createUnit(unitName: string) {
    this.getPage()
      .within(() => {
        cy.getByTestId('add-action-plan-line')
          .click();
        cy.getByTestId('action-input')
          .filter((k, el) => {
            return (el as HTMLInputElement).value.trim() === '';
          })
          .first()
          .type(unitName);
      });
    return this;
  }

  renameUnit(oldUnitName: string, newUnitName: string) {
    this.getPage()
      .within(() => {
        cy.getByTestId('action-input')
          .filter((k, el) => {
            return (el as HTMLInputElement).value.trim() === oldUnitName;
          })
          .first()
          .clear()
          .type(newUnitName);
      });
    return this;
  }

  renameFirstUnit(newUnitName: string) {
    this.getPage()
      .within(() => {
        cy.getByTestId('action-input')
          .first()
          .as('actionInput')
          .clear();
        cy.get('@actionInput')
          .type(newUnitName);
      });
    return this;
  }

  checkForDialogText() {
    cy.contains('Einheiten verwalten');
    cy.contains('Einheiten (Standard):');
    cy.contains('PROZENT (%)');
    cy.contains('ZAHL');
    cy.contains('CHF');
    cy.contains('EUR');
    cy.contains('FTE');
    cy.contains('Eigene Einheiten');
    cy.contains('Eigene Einheit hinzufügen');

    return this;
  }

  override submit() {
    this.getPage()
      .within(() => {
        cy.getByTestId('submit')
          .click();
      });
    return new KeyResultDialog();
  }

  override cancel() {
    this.getPage()
      .within(() => {
        cy.getByTestId('cancel')
          .click();
      });
    return new KeyResultDialog();
  }

  override close() {
    this.getPage()
      .within(() => {
        cy.getByTestId('close-dialog')
          .click();
      });
    return new KeyResultDialog();
  }

  getPage(): Chainable {
    return cy.get('app-manage-units-dialog');
  }
}
