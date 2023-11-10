import * as users from '../fixtures/users.json';

describe('Scoring component e2e tests', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  it('Create metric checkin and validate value of scoring component', () => {
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

  it('Create ordinal checkin and validate value of scoring component', () => {
    cy.createOrdinalKeyresult('Ordinal scoring keyresult', null);
    cy.getByTestId('keyresult').get(':contains("Ordinal scoring keyresult")').last().click();
    cy.getByTestId('add-check-in').click();
    cy.tabForward();
    cy.realPress('{downarrow}').realPress('{downarrow}');
    cy.getByTestId('target-zone').click();
    cy.tabForward();
    cy.realPress('{rightarrow}').realPress('{rightarrow}').realPress('{rightarrow}');
    cy.getByTestId('continue-checkin').click();
    cy.getByTestId('changeInfo').click().type('Testveränderungen');
    cy.getByTestId('initiatives').click().type('Testmassnahmen');
    cy.getByTestId('create-checkin').click();
  });
});
