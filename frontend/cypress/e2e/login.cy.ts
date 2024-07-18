import * as users from '../fixtures/users.json';

describe('OKR Login', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  it.skip('Login and check correct name is displayed', () => {
    console.log('***', cy.title());
    cy.title().should('equal', 'OKR Tool');
    cy.getByTestId('user-name').contains(users.gl.name);
  });

  it.skip('Login  and logout', () => {
    cy.title().should('equal', 'OKR Tool');
    cy.getByTestId('user-options').click();
    cy.getByTestId('logout').click();
    cy.origin(Cypress.env('login_url'), () => {
      cy.url().should('include', Cypress.env('login_url'));
      cy.get('#kc-page-title').contains('Sign in to your account');
    });
  });
});
