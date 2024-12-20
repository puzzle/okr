import { validateScoring } from "./helper/scoringSupport";
import { keyCodeDefinitions } from "cypress-real-events/keyCodeDefinitions";
import { doUntilSelector, pressUntilContains } from "./helper/utils";
import Chainable = Cypress.Chainable;

Cypress.Commands.add("loginAsUser", (user: any) => {
  loginWithCredentials(user.username, user.password);
  overviewIsLoaded();
});

Cypress.Commands.add("getByTestId", (testId: string, text?: string): Chainable => {
  const selector = `[data-testId=${testId}]`;

  if (text) {
    return cy.get(selector)
      .contains(text);
  } else {
    return cy.get(selector);
  }
});

Cypress.Commands.add("findByTestId", { prevSubject: true }, (subject: JQuery<HTMLElement>, testId: string, text?: string): Chainable => {
  const selector = `[data-testId=${testId}]`;
  if (text) {
    return cy.wrap(subject)
      .find(selector)
      .contains(text);
  } else {
    return cy.wrap(subject)
      .find(selector);
  }
});

Cypress.Commands.add("pressUntilContains", (text: string, key: keyof typeof keyCodeDefinitions) => {
  pressUntilContains(text, key);
});

Cypress.Commands.add("tabForward", () => {
  cy.realPress("Tab");
});

Cypress.Commands.add("tabBackward", () => {
  cy.realPress(["Shift",
    "Tab"]);
});

Cypress.Commands.add("tabForwardUntil", (selector: string, limit?: number) => {
  doUntilSelector(selector, cy.tabForward, limit);
});

Cypress.Commands.add("tabBackwardUntil", (selector: string, limit?: number) => {
  doUntilSelector(selector, cy.tabBackward, limit);
});

Cypress.Commands.add("getZone", (zone: string, onOverview: boolean) => {
  return (onOverview ? cy.focused() : cy.getByTestId("side-panel")).findByTestId(zone);
});

Cypress.Commands.add("validateScoring", (isOverview: boolean, percentage: number) => {
  validateScoring(isOverview, percentage);
});

function loginWithCredentials(username: string, password: string) {
  cy.visit("/");
  cy.intercept("GET", "**/users/current")
    .as("getCurrentUser");
  cy.origin(Cypress.env("login_url"), {
    args: {
      username,
      password
    }
  }, ({ username, password }) => {
    cy.get("input[name=\"username\"]")
      .type(username);
    cy.get("input[name=\"password\"]")
      .type(password);
    cy.get("button[type=\"submit\"]")
      .click();
    cy.wait("@getCurrentUser", { responseTimeout: 10000 });
  });
  cy.url()
    .then((url) => {
      const currentUrl = new URL(url);
      const baseURL = new URL(Cypress.config().baseUrl ?? "");
      expect(currentUrl.pathname)
        .equal(baseURL.pathname);
    });
}

const overviewIsLoaded = () => cy.get("mat-chip")
  .should("have.length.at.least", 2);

/*
 * -- This is a parent command --
 * Cypress.Commands.add("login", (email, password) => { ... })
 *
 *
 * -- This is a child command --
 * Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
 *
 *
 * -- This is a dual command --
 * Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
 *
 *
 * -- This will overwrite an existing command --
 * Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })
 */
