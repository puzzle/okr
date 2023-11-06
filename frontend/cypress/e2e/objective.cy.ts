import * as users from '../fixtures/users.json';

describe('OKR Objective e2e tests', () => {
  describe('tests via click', () => {
    beforeEach(() => {
      cy.loginAsUser(users.gl);
    });

    it('Create ongoing objective, no keyresults', () => {
      const objectiveTitle = 'This is a new ongoing objective by cypress';
      cy.getByTestId('create-objective').first().click();
      cy.getByTestId('title').first().type(objectiveTitle);
      cy.getByTestId('description').first().type('This is the description of the new Objective');
      cy.get('select#quarter').select('GJ 22/23-Q3');
      cy.getByTestId('safe').click();
      cy.visit('/?quarter=3');
      cy.contains(objectiveTitle);
      const objective = cy.contains(objectiveTitle).first().parentsUntil('#objective-column').last();
      objective.getByTestId('objective-state').should('have.attr', 'src', 'assets/icons/ongoing-icon.svg');
    });

    it('Create draft objective, no keyresults', () => {
      const objectiveTitle = 'This is a new draft objective by cypress';
      cy.getByTestId('create-objective').first().click();
      cy.getByTestId('title').first().type(objectiveTitle);
      cy.getByTestId('description').first().type('This is the description of the new Objective');
      cy.get('select#quarter').select('GJ 22/23-Q3');
      cy.getByTestId('safe-draft').click();
      cy.visit('/?quarter=3');
      const objective = cy.contains(objectiveTitle).first().parentsUntil('#objective-column').last();
      objective.getByTestId('objective-state').should('have.attr', 'src', 'assets/icons/draft-icon.svg');
    });
  });

  describe('tests via tab', () => {
    beforeEach(() => {
      cy.loginAsUser(users.gl);
    });
  });
});
