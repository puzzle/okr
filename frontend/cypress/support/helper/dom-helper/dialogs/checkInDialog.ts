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

  isZoneSelected(zone: 'fail' | 'commit' | 'target' | 'stretch') {
    const selector = zone + '-radio';
    cy.getByTestId(selector)
      .should('not.be.checked');
    return this;
  }

  fillCheckInInitiatives(value: string) {
    this.fillInputByTestId('initiatives', value);
    return this;
  }

  setCheckInConfidence(confidence: number) {
    for (let i = 0; i < 10; i++) {
      this.focusConfidenceSlider();
      cy.realPress('ArrowLeft');
    }
    for (let i = 0; i < confidence; i++) {
      this.focusConfidenceSlider();
      cy.realPress('ArrowRight');
    }
    return this;
  }

  private focusConfidenceSlider() {
    cy.getByTestId('confidence-slider')
      .as('slider');
    cy.get('@slider')
      .find('input')
      .focus();
  }

  checkForDialogTextMetric() {
    cy.contains('Check-in erfassen');
    cy.contains('Key Result');
    cy.contains('Neuer Wert');
    cy.contains('Confidence um Target Zone ');
    cy.contains('Abbrechen');
    return this;
  }

  checkForDialogTextOrdinal() {
    cy.contains('Check-in erfassen');
    cy.contains('Key Result');
    cy.contains('Fail:');
    cy.contains('Commit / Target / Stretch noch nicht erreicht');
    cy.contains('Commit:');
    cy.contains('Target:');
    cy.contains('Stretch:');
    cy.contains('Confidence um Target Zone zu erreichen');
    cy.contains('Abbrechen');
    return this;
  }

  addActionToActionPlan(action: string) {
    cy.getByTestId('add-action')
      .click();
    cy.getByTestId('action-input')
      .filter((k, el) => {
        return (el as HTMLInputElement).value.trim() === '';
      })
      .first()
      .type(action);
    cy.getByTestId('save-new-actions')
      .click();
    return this;
  }

  checkActionOfActionPlan(actionIndex: number) {
    cy.get('[id^=mat-mdc-checkbox-]')
      .filter((index, elem) => {
        // Keep only elements with an ID that matches the pattern
        return (/^mat-mdc-checkbox-\d+$/).test(elem.id);
      })
      .eq(actionIndex)
      .click();
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
