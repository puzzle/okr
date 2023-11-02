// ***********************************************
// This example namespace declaration will help
// with Intellisense and code completion in your
// IDE or Text Editor.
// ***********************************************
// declare namespace Cypress {
//   interface Chainable<Subject = any> {
//     customCommand(param: any): typeof customCommand;
//   }
// }
//
// function customCommand(param: any): void {
//   console.warn(param);
// }
//
// NOTE: You can use it like so:
// Cypress.Commands.add('customCommand', customCommand);
//
// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
Cypress.Commands.add('loginAsUser', (user: any) => {
  loginWithCredentials(user.username, user.password);
  overviewIsLoaded();
});

Cypress.Commands.add('getByTestId', (testId: string) => {
  return cy.get(`*[data-testId=${testId}]`);
});

Cypress.Commands.add('tabForward', () => {
  cy.realPress('Tab');
});

Cypress.Commands.add('tabBackward', () => {
  cy.realPress(['Shift', 'Tab']);
});

Cypress.Commands.add('tabForwardUntil', (selector: string, limit?: number) => {
  doUntil(selector, cy.tabForward, limit);
});

Cypress.Commands.add('tabBackwardUntil', (selector: string, limit?: number) => {
  doUntil(selector, cy.tabBackward, limit);
});

function doUntil(selector: string, tab: () => void, limit: number = 100) {
  for (let i = 0; i < limit; i++) {
    cy.focused().then((element) => {
      if (element.get(0).matches(selector)) {
        return;
      } else {
        tab();
      }
    });
  }
}

function loginWithCredentials(username: string, password: string) {
  cy.visit('/');
  cy.origin(
    'https://idp-mock-okr.ocp-internal.cloudscale.puzzle.ch',
    { args: { username, password } },
    ({ username, password }) => {
      cy.get('input[name="username"]').type(username);
      cy.get('input[name="password"]').type(password);
      cy.get('input[type="submit"]').click();
    },
  );
  cy.url().then((url) => {
    const currentUrl = new URL(url);
    const baseURL = new URL(Cypress.config().baseUrl!);
    expect(currentUrl.pathname).equal(baseURL.pathname);
  });
}

const overviewIsLoaded = () =>
  cy.get('mat-chip').should('have.length.at.least', 2) && cy.get('.team-title').should('have.length.at.least', 1);

// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })
