Cypress.Commands.add('loginWithCredentials', (username, password) => {
  cy.visit('/');

  cy.get('input[name="login"]').type(username);
  cy.get('input[name="password"]').type(password);
  cy.get('button[type="submit"]').click();
  cy.get('button[type="submit"]').click();
});
