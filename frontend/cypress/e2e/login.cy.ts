import * as users from '../fixtures/users.json';

describe('okr login', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  it('should display correct username after login', () => {
    cy.title()
      .should('equal', 'Puzzle OKR');
    cy.getByTestId('username')
      .contains(users.gl.name);
  });

  it('should login and logout', () => {
    cy.logout();
    cy.origin(Cypress.env('LOGIN_URL'), () => {
      cy.get('#kc-page-title')
        .contains('Sign in to your account');
      cy.url()
        .should('include', Cypress.env('LOGIN_URL'));
    });
  });

  it('should open correct help page when clicking help button', () => {
    cy.window()
      .then((win) => {
        cy.stub(win, 'open')
          .as('openWindow');
      });

    cy.get('#help-button')
      .click();
    cy.get('@openWindow')
      .should('be.calledWith', 'https://wiki.puzzle.ch/Puzzle/OKRs');
  });
});
