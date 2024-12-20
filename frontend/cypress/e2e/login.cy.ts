import * as users from '../fixtures/users.json';

describe('OKR Login', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  it('Login and check correct name is displayed', () => {
    cy.title()
      .should('equal', 'Puzzle OKR');
    cy.getByTestId('user-name')
      .contains(users.gl.name);
  });

  it('Login  and logout', () => {
    cy.title()
      .should('equal', 'Puzzle OKR');
    cy.getByTestId('user-options')
      .click();
    cy.getByTestId('logout')
      .click();
    cy.origin(Cypress.env('login_url'), () => {
      cy.url()
        .should('include', Cypress.env('login_url'));
      cy.get('#kc-page-title')
        .contains('Sign in to your account');
    });
  });

  it('Click on the Hilfe button should open a new tab with the correct URL', () => {
    cy.window()
      .then((win) => {
        cy.stub(win, 'open')
          .as('openWindow');
      });

    cy.get('#hilfeButton')
      .click();
    cy.get('@openWindow')
      .should('be.calledWith', 'https://wiki.puzzle.ch/Puzzle/OKRs');
  });
});
