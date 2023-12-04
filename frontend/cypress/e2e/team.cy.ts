import * as users from '../fixtures/users.json';

describe('OKR team e2e tests', () => {
  describe('tests via click', () => {
    beforeEach(() => {
      cy.loginAsUser(users.gl);
      cy.visit('/?quarter=2');
    });

    describe('team filter', () => {
      it('Deselect teams from filter', () => {
        cy.get('p:visible:contains("Puzzle ITC")').should('have.length', 1);
        cy.get('mat-chip:visible:contains("Puzzle ITC")').should('have.length', 1);
        cy.get('mat-chip:visible:contains("Alle")').click();
        cy.contains('Alle');
        cy.get('p:visible:contains("/BBT")').should('have.length', 1);
        cy.get('mat-chip:visible:contains("/BBT")').should('have.length', 1);
        cy.get('p:visible:contains("Puzzle ITC")').should('have.length', 1);
        cy.get('mat-chip:visible:contains("Puzzle ITC")').should('have.length', 1);
        cy.get('p:visible:contains("LoremIpsum")').should('have.length', 1);
        cy.get('mat-chip:visible:contains("LoremIpsum")').should('have.length', 1);
        cy.get('p:visible:contains("we are cube")').should('have.length', 1);
        cy.get('mat-chip:visible:contains("we are cube")').should('have.length', 1);

        cy.getByTestId('team-filter-alle').should('have.attr', 'ng-reflect-highlighted', 'true');
        cy.get('mat-chip:visible:contains("/BBT")').should('have.attr', 'ng-reflect-highlighted', 'true');
        cy.get('mat-chip:visible:contains("/BBT")').click();
        cy.get('p:visible:contains("/BBT")').should('exist');
        cy.get('p:visible:contains("Puzzle ITC")').should('not.exist');
        cy.get('p:visible:contains("LoremIpsum")').should('not.exist');
        cy.get('p:visible:contains("we are cube")').should('not.exist');
        cy.get('mat-chip:visible:contains("/BBT")').should('have.attr', 'ng-reflect-highlighted', 'true');
        cy.get('mat-chip:visible:contains("Puzzle ITC")').should('have.attr', 'ng-reflect-highlighted', 'false');
        cy.get('mat-chip:visible:contains("LoremIpsum")').should('have.attr', 'ng-reflect-highlighted', 'false');
        cy.get('mat-chip:visible:contains("we are cube")').should('have.attr', 'ng-reflect-highlighted', 'false');
        cy.getByTestId('team-filter-alle').should('have.attr', 'ng-reflect-highlighted', 'false');

        cy.get('mat-chip:visible:contains("Puzzle ITC")').click();
        cy.get('p:visible:contains("Puzzle ITC")').should('exist');
        cy.get('p:visible:contains("/BBT")').should('exist');
        cy.get('p:visible:contains("LoremIpsum")').should('not.exist');
        cy.get('p:visible:contains("we are cube")').should('not.exist');
        cy.get('mat-chip:visible:contains("/BBT")').should('have.attr', 'ng-reflect-highlighted', 'true');
        cy.get('mat-chip:visible:contains("Puzzle ITC")').should('have.attr', 'ng-reflect-highlighted', 'true');
        cy.get('mat-chip:visible:contains("LoremIpsum")').should('have.attr', 'ng-reflect-highlighted', 'false');
        cy.get('mat-chip:visible:contains("we are cube")').should('have.attr', 'ng-reflect-highlighted', 'false');
        cy.getByTestId('team-filter-alle').should('have.attr', 'ng-reflect-highlighted', 'false');
      });

      it('Deselect all teams from filter will display text on overview', () => {
        cy.getByTestId('team-filter-alle').should('have.attr', 'ng-reflect-highlighted', 'false');
        cy.get('mat-chip:visible:contains("/BBT")').should('have.attr', 'ng-reflect-highlighted', 'false');
        cy.get('mat-chip:visible:contains("Puzzle ITC")').should('have.attr', 'ng-reflect-highlighted', 'true');
        cy.get('mat-chip:visible:contains("LoremIpsum")').should('have.attr', 'ng-reflect-highlighted', 'false');
        cy.get('mat-chip:visible:contains("we are cube")').should('have.attr', 'ng-reflect-highlighted', 'false');
        cy.get('mat-chip:visible:contains("Puzzle ITC")').click();
        cy.contains('Kein Team ausgewählt');
      });

      it('URL changes to the selected teams', () => {
        cy.url().should('include', 'teams=');
        cy.url().should('include', '5');
        cy.url().should('not.include', '6');

        cy.get('mat-chip:visible:contains("LoremIpsum")').click();
        cy.url().should('include', 'teams=');
        cy.url().should('include', '5');
        cy.url().should('include', '6');
        cy.get('mat-chip:visible:contains("Puzzle ITC")').click();
        cy.get('mat-chip:visible:contains("LoremIpsum")').click();
        cy.url().should('not.include', 'teams=');
        cy.url().should('not.include', '5');
        cy.url().should('not.include', '6');
      });

      it('Select teams by url', () => {
        cy.url().should('not.include', 'team');

        cy.visit('/?quarter=2&teams=4,5,8');
        cy.wait(1000);
        cy.getByTestId('team-filter-alle').should('have.attr', 'ng-reflect-highlighted', 'false');
        cy.get('mat-chip:visible:contains("/BBT")').should('have.attr', 'ng-reflect-highlighted', 'true');
        cy.get('mat-chip:visible:contains("Puzzle ITC")').should('have.attr', 'ng-reflect-highlighted', 'true');
        cy.get('mat-chip:visible:contains("LoremIpsum")').should('have.attr', 'ng-reflect-highlighted', 'false');
        cy.get('mat-chip:visible:contains("we are cube")').should('have.attr', 'ng-reflect-highlighted', 'true');
        cy.get('p:visible:contains("LoremIpsum")').should('not.exist');
      });
    });
  });
});
