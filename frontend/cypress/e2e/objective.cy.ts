import * as users from '../fixtures/users.json';

describe('OKR Objective e2e tests', () => {
  describe('tests via click', () => {
    beforeEach(() => {
      cy.loginAsUser(users.gl);
    });

    [
      ['ongoing objective title', 'safe', 'ongoing-icon.svg'],
      ['draft objective title', 'safe-draft', 'draft-icon.svg'],
    ].forEach(([objectiveTitle, buttonTestId, icon]) => {
      it(`Create ${buttonTestId} objective, no keyresults`, () => {
        cy.getByTestId('create-objective').first().click();
        cy.getByTestId('title').first().type(objectiveTitle);
        cy.getByTestId('description').first().type('This is the description of the new Objective');
        cy.get('select#quarter').select('GJ 22/23-Q3');
        cy.getByTestId(buttonTestId).click();
        cy.visit('/?quarter=3');
        const objective = cy.contains(objectiveTitle).first().parentsUntil('#objective-column').last();
        objective.getByTestId('objective-state').should('have.attr', 'src', `assets/icons/${icon}`);
      });
    });
  });

  describe('tests via tab', () => {
    beforeEach(() => {
      cy.loginAsUser(users.gl);
    });
  });
});
