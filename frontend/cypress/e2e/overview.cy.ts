import * as users from '../fixtures/users.json';

describe('OKR Overview', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  it('Check order of teams', () => {
    cy.intercept('GET', '**/overview*').as('overview');

    cy.get('mat-chip:visible:contains("Alle")').click();
    cy.wait('@overview');
    const textsExpectedOrder = ['LoremIpsum', 'Puzzle ITC', '/BBT', 'we are cube.³'];
    cy.get('.team-title').then((elements) => {
      const texts: string[] = elements.map((_, el) => Cypress.$(el).text()).get();
      expect(texts).to.deep.equal(textsExpectedOrder);
    });
  });

  it('Check font ', () => {
    cy.get('.team-title').first().invoke('css', 'font-family').should('eq', 'Roboto, sans-serif');
    cy.get('.team-title').first().invoke('css', 'font-weight').should('eq', '700');
  });
});
