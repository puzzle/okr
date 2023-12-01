import * as users from '../fixtures/users.json';
import { getPercentageMetric, getPercentageOrdinal } from 'cypress/support/scoringSupport';
import { onlyOn } from '@cypress/skip-test';

describe('Scoring component e2e tests', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    onlyOn('chrome');
    cy.visit('/?quarter=2');
  });

  [
    [0, 100, 10],
    [0, 100, 31],
    [0, 100, 71],
    [0, 100, 100],
  ].forEach(([baseline, stretchgoal, value]) => {
    it('Create metric checkin and validate value of scoring component', () => {
      cy.createMetricKeyresult('Metric scoring keyresult', String(baseline), String(stretchgoal));
      cy.getByTestId('keyresult').get(':contains("Metric scoring keyresult")').last().click();
      cy.getByTestId('add-check-in').click();
      cy.getByTestId('key-result-metric-value').clear().type(String(value));
      cy.getByTestId('confidence-slider').click();
      cy.realPress('{rightarrow}').realPress('{rightarrow}').realPress('{rightarrow}');
      cy.getByTestId('changeInfo').click().type('Testveränderungen');
      cy.getByTestId('initiatives').click().type('Testmassnahmen');
      cy.getByTestId('submit-check-in').click();
      const percentage = getPercentageMetric(baseline, stretchgoal, value);
      cy.validateScoring(false, percentage);
      cy.getByTestId('close-drawer').click({ force: true });
      cy.validateScoring(true, percentage);
    });
  });

  [['fail'], ['commit'], ['target'], ['stretch']].forEach(([zoneName]) => {
    it('Create ordinal checkin and validate value of scoring component', () => {
      cy.createOrdinalKeyresult('Ordinal scoring keyresult', null);
      cy.getByTestId('keyresult').get(':contains("Ordinal scoring keyresult")').last().click();
      cy.getByTestId('add-check-in').click();
      cy.getByTestId(`${zoneName}-radio`).click();
      cy.getByTestId('confidence-slider').click();
      cy.realPress('{rightarrow}').realPress('{rightarrow}').realPress('{rightarrow}');
      cy.getByTestId('changeInfo').click().type('Testveränderungen');
      cy.getByTestId('initiatives').click().type('Testmassnahmen');
      cy.getByTestId('submit-check-in').click();
      const percentage = getPercentageOrdinal(zoneName);
      cy.validateScoring(false, percentage);
      cy.getByTestId('close-drawer').click({ force: true });
      cy.validateScoring(true, percentage);
    });
  });
});
