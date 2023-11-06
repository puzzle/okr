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
      it(`Create objective, no keyresults`, () => {
        cy.createObjective(objectiveTitle, 'GJ 22/23-Q3', buttonTestId);
        cy.visit('/?quarter=3');
        const objective = cy.contains(objectiveTitle).first().parentsUntil('#objective-column').last();
        objective.getByTestId('objective-state').should('have.attr', 'src', `assets/icons/${icon}`);
      });
    });

    [
      ['ongoing objective title', 'safe', 'ongoing-icon.svg'],
      ['draft objective title', 'safe-draft', 'draft-icon.svg'],
    ].forEach(([objectiveTitle, buttonTestId, icon]) => {
      it(`Create objective, no keyresults`, () => {
        cy.createObjective(objectiveTitle, 'GJ 22/23-Q3', buttonTestId, true);
        cy.contains('Key Result erfassen');
      });
    });

    it(`Create objective, cancel`, () => {
      const objectiveTitle = 'this is a canceled objective';
      cy.createObjective(objectiveTitle, 'GJ 22/23-Q3', 'cancel');
      cy.visit('/?quarter=3');
      cy.contains(objectiveTitle).should('not.exist');
    });

    it(`Delete existing objective`, () => {
      cy.get('app-objective-column').first().getByTestId('three-dot-menu').click();
      cy.get('.mat-mdc-menu-content').contains('Objective bearbeiten').click();
      cy.getByTestId('delete').click();
      cy.get("button[type='submit']").contains('Ja').click();
    });
  });
});

describe('tests via tab', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });
});
