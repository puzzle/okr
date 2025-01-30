import Dialog from './dialog';
import ConfirmDialog from './confirmDialog';
import Chainable = Cypress.Chainable;

export default class KeyResultDialog extends Dialog {
  fillKeyResultTitle(title: string) {
    this.fillInputByTestId('title-input', title);
    return this;
  }

  fillKeyResultDescription(description: string) {
    this.fillInputByTestId('description-input', description);
    return this;
  }

  withMetricValues(
    unit: string, baseline?: string, targetGoal?: string, stretchGoal?: string
  ) {
    cy.getByTestId('metric-tab')
      .click();

    cy.getByTestId('unit')
      .click()
      .clear()
      .type(unit);

    cy.realPress('ArrowDown')
      .realPress('Enter');

    if (baseline !== undefined) {
      this.fillInputByTestId('baseline', baseline);
    }
    if (targetGoal !== undefined) {
      this.fillInputByTestId('target-goal', targetGoal);
    }
    if (stretchGoal !== undefined) {
      this.fillInputByTestId('stretch-goal', stretchGoal);
    }
    return this;
  }

  editMetricValue(newValue: string, indexOfInputField: number) {
    this.openEditMetricValues();
    this.editUnitWithIndex(newValue, indexOfInputField);
    return this;
  }

  withOrdinalValues(commitZone: string, targetZone: string, stretchGoal: string) {
    cy.getByTestId('ordinal-tab')
      .click();
    this.fillInputByTestId('commit-zone', commitZone);
    this.fillInputByTestId('target-zone', targetZone);
    this.fillInputByTestId('stretch-zone', stretchGoal);
    return this;
  }

  fillOwner(owner: string) {
    this.fillInputByTestId('owner-input', owner);
    cy.realPress('ArrowDown')
      .realPress('Enter');
    return this;
  }

  addActionPlanElement(action: string) {
    cy.getByTestId('add-action-plan-line')
      .click();
    cy.getByTestId('action-input')
      .filter((k, el) => {
        return (el as HTMLInputElement).value.trim() === '';
      })
      .first()
      .type(action);
    return this;
  }

  deleteKeyResult() {
    cy.getByTestId('delete-key-result')
      .click();
    return new ConfirmDialog();
  }

  checkForDialogTextMetric() {
    cy.contains('Einheit');
    cy.contains('Baseline');
    cy.contains('Target Goal');
    cy.contains('Stretch Goal');
    this.checkForDialogText();
    return this;
  }

  checkForDialogTextOrdinal() {
    cy.contains('Commit Zone');
    cy.contains('Target Zone');
    cy.contains('Stretch Goal');
    this.checkForDialogText();
    return this;
  }

  private checkForDialogText() {
    cy.contains('Key Result erfassen');
    cy.contains('Titel');
    cy.contains('Metrisch');
    cy.contains('Ordinal');
    cy.contains('Owner');
    cy.contains('Beschreibung (optional)');
    cy.contains('Action Plan (optional)');
    cy.contains('Weitere Action hinzufügen');
    cy.contains('Speichern');
    cy.contains('Speichern & Neu');
    cy.contains('Abbrechen');
  }

  private openEditMetricValues() {
    cy.getByTestId('manage-units', 'Einheiten Verwalten')
      .click();

    cy.contains('Einheiten verwalten');
    cy.contains('Einheiten (Standard):');
    cy.contains('PERCENT (%)');
    cy.contains('NUMBER');
    cy.contains('CHF');
    cy.contains('EUR');
    cy.contains('FTE');
    cy.contains('Eigene Einheiten');
    cy.contains('Eigene Einheit hinzufügen');
  }

  private editUnitWithIndex(newValue: string, index: number) {
    cy.get('app-dialog-template-core')
      .filter('[ng-reflect-title="Einheiten verwalten"]')
      .within(() => {
        const input = cy.getByTestId('action-input')
          .eq(index)
          .click();

        input
          .clear()
          .type(newValue);

        cy.getByTestId('submit')
          .click();
      });
  }

  override submit() {
    cy.getByTestId('submit')
      .click();
  }

  saveAndNew() {
    cy.getByTestId('save-and-new')
      .click();
  }

  getPage(): Chainable {
    return cy.get('app-key-result-form');
  }
}
