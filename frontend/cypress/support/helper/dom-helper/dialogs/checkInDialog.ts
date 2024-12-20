import Dialog from './dialog';
import Chainable = Cypress.Chainable;

export default class CheckInDialog extends Dialog {
  fillCheckInCommentary(commentary: string) {
    this.fillInputByTestId('changeInfo', commentary);
    return this;
  }

  fillMetricCheckInValue(value: string) {
    this.fillInputByTestId('check-in-metric-value', value);
    return this;
  }

  selectOrdinalCheckInZone(zone: 'fail' | 'commit' | 'target' | 'stretch') {
    switch (zone) {
      case 'fail':
        cy.getByTestId('fail-radio')
          .click();
        break;
      case 'commit':
        cy.getByTestId('commit-radio')
          .click();
        break;
      case 'target':
        cy.getByTestId('target-radio')
          .click();
        break;
      case 'stretch':
        cy.getByTestId('stretch-radio')
          .click();
        break;
    }
    return this;
  }

  fillCheckInInitiatives(value: string) {
    this.fillInputByTestId('initiatives', value);
    return this;
  }

  setCheckInConfidence(confidence: number) {
    cy.getByTestId('confidence-slider')
      .find('input')
      .focus();
    for (let i = 0; i < 10; i++) {
      cy.realPress('ArrowLeft');
    }
    for (let i = 0; i < confidence; i++) {
      cy.realPress('ArrowRight');
    }
    return this;
  }

  checkForDialogTextMetric() {
    cy.contains('Very important keyresult');
    cy.contains('Check-in erfassen');
    cy.contains('Key Result');
    cy.contains('Neuer Wert');
    cy.contains('Confidence um Target Zone (42%) zu erreichen');
    cy.contains('Abbrechen');
    return this;
  }

  checkForDialogTextOrdinal() {
    cy.contains('A new ordinal keyresult for our company');
    cy.contains('Check-in erfassen');
    cy.contains('Key Result');
    cy.contains('Fail:');
    cy.contains('Commit / Target / Stretch noch nicht erreicht');
    cy.contains('Commit:');
    cy.contains('Target:');
    cy.contains('Stretch:');
    cy.contains('New car');
    cy.contains('New house');
    cy.contains('New pool');
    cy.contains('Confidence um Target Zone zu erreichen');
    cy.contains('Abbrechen');
    return this;
  }

  override submit() {
    cy.getByTestId('submit-check-in')
      .click();
  }

  getPage(): Chainable {
    return cy.get('app-check-in-form');
  }
}
