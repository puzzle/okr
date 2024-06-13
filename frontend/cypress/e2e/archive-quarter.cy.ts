import * as users from '../fixtures/users.json';
import { onlyOn } from '@cypress/skip-test';

describe('OKR Archive Quarter e2e tests', () => {
  describe('tests via click', () => {
    beforeEach(() => {
      cy.loginAsUser(users.gl);
      cy.visit('/?quarter=2');
      onlyOn('chrome');
    });

    it(`Should display past objects in archive without possibility to edit`, () => {
      cy.visit('/?quarter=998');
      cy.contains('Keine Daten im Archiv');
      cy.visit('/?quarter=998&teams=5,4,6,8');
      cy.contains('This objective is in backlog');
      cy.contains('KeyResult in archive');
      cy.contains('LoremIpsum');
      cy.contains('Objective hinzufügen').should('not.exist');
      cy.contains('Key Result hinzufügen').should('not.exist');

      cy.getByTestId('objective').first().getByTestId('three-dot-menu').should('not.exist');

      cy.getByTestId('objective').first().click();
      cy.contains('Key Result bearbeiten').should('not.exist');
      cy.contains('Check-in erfassen').should('not.exist');
    });
  });
});
