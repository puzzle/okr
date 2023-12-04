import * as users from '../fixtures/users.json';
import { onlyOn } from '@cypress/skip-test';

describe('e2e test for scoring adjustment on objective duplicate', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    onlyOn('chrome');
    cy.visit('/?quarter=2');
  });

  it('Create ordinal checkin and validate value of scoring component', () => {
    cy.createOrdinalKeyresult('stretch keyresult for testing', null);
    cy.getByTestId('keyresult').get(':contains("stretch keyresult for testing")').last().click();
    cy.getByTestId('add-check-in').click();
    cy.getByTestId(`stretch-radio`).click();
    cy.getByTestId('confidence-slider').click();
    cy.realPress('{rightarrow}').realPress('{rightarrow}').realPress('{rightarrow}');
    cy.getByTestId('changeInfo').click().type('Testveränderungen');
    cy.getByTestId('initiatives').click().type('Testmassnahmen');
    cy.getByTestId('submit-check-in').click();
    cy.getByTestId('close-drawer').click({ force: true });

    cy.get('.objective').first().getByTestId('three-dot-menu').click();
    cy.get('.mat-mdc-menu-content').contains('Objective duplizieren').click();
    cy.fillOutObjective('A duplicated Objective for this tool', 'safe', '3');
    cy.visit('/?quarter=3');

    let scoringBlock1 = cy
      .getByTestId('objective')
      .first()
      .getByTestId('key-result')
      .first()
      .getByTestId('scoring-component')
      .first();

    scoringBlock1.getByTestId('fail').first().should('not.have.css', 'score-red');
    scoringBlock1.getByTestId('fail').first().should('not.have.css', 'score-yellow');
    scoringBlock1.getByTestId('fail').first().should('not.have.css', 'score-green');
    scoringBlock1.getByTestId('fail').first().should('not.have.css', 'score-stretch');

    let scoringBlock2 = cy
      .getByTestId('objective')
      .first()
      .getByTestId('key-result')
      .last()
      .getByTestId('scoring-component')
      .last();

    scoringBlock2.getByTestId('fail').first().should('not.have.css', 'score-red');
    scoringBlock2.getByTestId('fail').first().should('not.have.css', 'score-yellow');
    scoringBlock2.getByTestId('fail').first().should('not.have.css', 'score-green');
    scoringBlock2.getByTestId('fail').first().should('not.have.css', 'score-stretch');
  });
});
