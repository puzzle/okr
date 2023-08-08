describe('template spec', () => {
  it('passes', () => {
    cy.visit('http://localhost:4200');

    cy.origin('https://sso.puzzle.ch', () => {
      cy.contains('Sign in to your account');
      cy.get('#social-idp-members').click();
      cy.contains('Username or email');
    });
  });
});
