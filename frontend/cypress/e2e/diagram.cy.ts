import * as users from '../fixtures/users.json';

describe('OKR diagram e2e tests', () => {
  describe('tests via click', () => {
    beforeEach(() => {
      cy.loginAsUser(users.gl);
      cy.visit('/?quarter=10');
      cy.getByTestId('add-objective').first().click();
      cy.fillOutObjective('An Objective for Testing', 'safe-draft', '10');
    });

    it('Can switch to diagram with the tab-switch', () => {
      cy.get('h1:contains(Puzzle ITC)').should('have.length', 1);
      cy.get('mat-chip:visible:contains("Puzzle ITC")').should('have.length', 1);
      cy.contains('Overview');
      cy.contains('Network');
      cy.contains('An Objective for Testing');

      cy.getByTestId('diagramTab').first().click();

      cy.contains('Kein Alignment vorhanden');
      cy.get('h1:contains(Puzzle ITC)').should('have.length', 0);
      cy.get('mat-chip:visible:contains("Puzzle ITC")').should('have.length', 1);
      cy.getByTestId('objective').should('have.length', 0);

      cy.getByTestId('overviewTab').first().click();

      cy.get('h1:contains(Puzzle ITC)').should('have.length', 1);
      cy.get('mat-chip:visible:contains("Puzzle ITC")').should('have.length', 1);
      cy.contains('An Objective for Testing');
    });

    it('Can switch to diagram and the filter stay the same', () => {
      cy.get('h1:contains(Puzzle ITC)').should('have.length', 1);
      cy.get('mat-chip:visible:contains("Puzzle ITC")').should('have.length', 1);
      cy.contains('Overview');
      cy.contains('Network');
      cy.contains('An Objective for Testing');
      cy.getByTestId('quarterFilter').should('contain', 'GJ 24/25-Q1');
      cy.get('mat-chip:visible:contains("Puzzle ITC")')
        .should('have.css', 'background-color')
        .and('eq', 'rgb(30, 90, 150)');
      cy.get('mat-chip:visible:contains("/BBT")')
        .should('have.css', 'background-color')
        .and('eq', 'rgb(255, 255, 255)');

      cy.get('mat-chip:visible:contains("/BBT")').click();
      cy.get('mat-chip:visible:contains("Puzzle ITC")').click();

      cy.getByTestId('diagramTab').first().click();

      cy.contains('Kein Alignment vorhanden');
      cy.get('h1:contains(Puzzle ITC)').should('have.length', 0);
      cy.get('mat-chip:visible:contains("Puzzle ITC")').should('have.length', 1);
      cy.getByTestId('quarterFilter').should('contain', 'GJ 24/25-Q1');
      cy.getByTestId('objective').should('have.length', 0);
      cy.get('mat-chip:visible:contains("/BBT")').should('have.css', 'background-color').and('eq', 'rgb(30, 90, 150)');
      cy.get('mat-chip:visible:contains("Puzzle ITC")')
        .should('have.css', 'background-color')
        .and('eq', 'rgb(255, 255, 255)');
      cy.get('mat-chip:visible:contains("Puzzle ITC")').click();

      cy.getByTestId('quarterFilter').first().focus();
      cy.focused().realPress('ArrowDown');

      cy.getByTestId('quarterFilter').should('contain', 'GJ 23/24-Q4');
      cy.get('canvas').should('have.length', 3);

      cy.getByTestId('overviewTab').first().click();

      cy.get('mat-chip:visible:contains("/BBT")').should('have.css', 'background-color').and('eq', 'rgb(30, 90, 150)');
      cy.get('mat-chip:visible:contains("Puzzle ITC")')
        .should('have.css', 'background-color')
        .and('eq', 'rgb(30, 90, 150)');
      cy.getByTestId('quarterFilter').should('contain', 'GJ 23/24-Q4');
    });
  });
});
