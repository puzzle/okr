import * as users from '../fixtures/users.json';

describe('Scoring component e2e tests', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  it('Creates metric checkin', () => {
    cy.createMetricKeyresult('Metric scoring keyresult');
    cy.getByTestId('keyresult').get(':contains("Metric scoring keyresult")').last().click();
    cy.getByTestId('add-check-in').click();
    cy.getByTestId('key-result-metric-value').clear().type('30');
    cy.tabForward();
    cy.realPress('{rightarrow}').realPress('{rightarrow}').realPress('{rightarrow}');
    cy.getByTestId('continue-checkin').click();
    cy.getByTestId('changeInfo').click().type('Testveränderungen');
    cy.getByTestId('initiatives').click().type('Testmassnahmen');
    cy.getByTestId('create-checkin').click();
  });
});
