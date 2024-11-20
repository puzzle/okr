import * as users from '../fixtures/users.json';
import FilterHelper from '../support/helper/dom-helper/filterHelper';

describe('OKR Overview', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  it('should have the current quarter with label Aktuell', () => {
    cy.visit('/?quarter=2');
    cy.getByTestId('quarterFilter').contains('Aktuell');
  });

  it('Check order of teams', () => {
    FilterHelper.do().toggleOption('Alle');
    const textsExpectedOrder = ['LoremIpsum', 'Puzzle ITC', '/BBT', 'we are cube.³'];
    cy.get('.team-title:contains("we are cube.³")');
    cy.get('.team-title').then((elements) => {
      const texts: string[] = elements.map((_, el) => Cypress.$(el).text()).get();
      expect(texts).to.deep.equal(textsExpectedOrder);
    });
  });

  it('Check font ', () => {
    cy.get('.team-title').first().invoke('css', 'font-family').should('eq', 'Roboto, sans-serif');
    cy.get('.team-title').first().invoke('css', 'font-weight').should('eq', '600');
  });
});
