import * as users from '../fixtures/users.json';

describe('OKR Login', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  it('Login and check correct name is displayed', () => {
    cy.title().should('equal', 'Puzzle OKR');
    cy.get("pzsh-menu-dropdown > div[slot='toggle']").contains(users.gl.name);
  });

  it('Login  and logout', () => {
    cy.title().should('equal', 'Puzzle OKR');
    cy.get("pzsh-menu-dropdown > div[slot='toggle']").click();
    cy.getByTestId('logout').click();
    cy.origin(Cypress.env('login_url'), () => {
      cy.url().should('include', Cypress.env('login_url'));
      cy.get('#kc-page-title').contains('Sign in to your account');
    });
  });
});
