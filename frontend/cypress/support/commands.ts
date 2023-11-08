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

Cypress.Commands.add(
  'fillOutKeyResult',
  (
    title: string,
    unit: string | null,
    baseline: string | null,
    stretchGoal: string | null,
    commitZone: string | null,
    targetZone: string | null,
    stretchZone: string | null,
    owner: string,
    description: string,
  ) => {
    cy.getByTestId('titleInput').clear().type(title);
    if (commitZone == null) {
      cy.getByTestId('unit').select(unit!);
      cy.getByTestId('baseline').clear().type(baseline!);
      cy.getByTestId('stretchGoal').clear().type(stretchGoal!);
    } else {
      cy.getByTestId('commitZone').clear().type(commitZone!);
      cy.getByTestId('targetZone').clear().type(targetZone!);
      cy.getByTestId('stretchZone').clear().type(stretchZone!);
    }
    cy.getByTestId('ownerInput').clear().type(owner).type('{downarrow}').type('{enter}');
    cy.getByTestId('descriptionInput').clear().type(description);
  },
);

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
  cy.origin(Cypress.env('login_url'), { args: { username, password } }, ({ username, password }) => {
    cy.get('input[name="username"]').type(username);
    cy.get('input[name="password"]').type(password);
    cy.get('input[type="submit"]').click();
  });
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
