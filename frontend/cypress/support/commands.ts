import { validateScoring } from './scoringSupport';

Cypress.Commands.add('loginAsUser', (user: any) => {
  loginWithCredentials(user.username, user.password);
  overviewIsLoaded();
});

Cypress.Commands.add('getByTestId', { prevSubject: 'optional' }, (subject: any, testId: string) => {
  if (subject) {
    return cy.wrap(subject).find(`[data-testId=${testId}]`);
  }
  return cy.get(`[data-testId=${testId}]`);
});

Cypress.Commands.add('getZone', (zone: string, onOverview: boolean) => {
  return (onOverview ? cy.focused() : cy.getByTestId('side-panel')).getByTestId(zone);
});

Cypress.Commands.add('validateScoring', (isOverview: boolean, percentage: number) => {
  validateScoring(isOverview, percentage);
});

Cypress.Commands.add('checkForErrorToaster', (amount: number, errorMessages?: string[]) => {
  checkForToaster('toast-error', amount, errorMessages);
});

Cypress.Commands.add('checkForWarnToaster', (amount: number, errorMessages?: string[]) => {
  checkForToaster('toast-warn', amount, errorMessages);
});

Cypress.Commands.add('checkForSuccessToaster', (amount: number, errorMessages?: string[]) => {
  checkForToaster('toast-success', amount, errorMessages);
});

Cypress.Commands.add(
  'fillOutObjective',
  (
    objectiveTitle: string,
    button: string,
    quarter?: string,
    description?: string,
    createKeyResults: boolean = false,
  ) => {
    cy.getByTestId('title').first().clear().type(objectiveTitle);
    cy.getByTestId('description')
      .first()
      .clear()
      .type(description || 'This is the description of the new Objective');
    if (quarter) {
      cy.get('select#quarter').select(quarter);
    }
    if (createKeyResults) {
      cy.getByTestId('keyResult-checkbox').find("[type='checkbox']").check();
    }
    cy.getByTestId(button).click();
  },
);

Cypress.Commands.add(
  'fillOutCheckInMetric',
  (currentValue: number, shouldChangeConfidence: number, changeInfo: string | null, initiatives: string | null) => {
    cy.getByTestId('check-in-metric-value').clear().type(currentValue.toString());
    setConfidence(shouldChangeConfidence);
    if (changeInfo) {
      cy.getByTestId('changeInfo').clear().type(changeInfo!);
    }
    if (initiatives) {
      cy.getByTestId('initiatives').clear().type(initiatives!);
    }
    cy.getByTestId('submit-check-in').click();
  },
);

Cypress.Commands.add(
  'fillOutCheckInOrdinal',
  (currentZoneIndex: number, confidence: number, changeInfo: string | null, initiatives: string | null) => {
    switch (currentZoneIndex) {
      case 0:
        cy.getByTestId('fail-radio').click();
        break;
      case 1:
        cy.getByTestId('commit-radio').click();
        break;
      case 2:
        cy.getByTestId('target-radio').click();
        break;
      case 3:
        cy.getByTestId('stretch-radio').click();
        break;
    }
    setConfidence(confidence);
    if (changeInfo) {
      cy.getByTestId('changeInfo').clear().type(changeInfo!);
    }
    if (initiatives) {
      cy.getByTestId('initiatives').clear().type(initiatives!);
    }
    cy.getByTestId('submit-check-in').click();
  },
);

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

Cypress.Commands.add('createOrdinalKeyresult', (title: string | null = null, owner: string | null = null) => {
  cy.getByTestId('objective').first().getByTestId('add-keyResult').first().click();
  cy.getByTestId('submit').should('be.disabled');
  cy.contains('Key Result erfassen');
  cy.contains('Jaya Norris');
  cy.contains('Titel');
  cy.getByTestId('titleInput').type('Title');
  cy.getByTestId('ordinalTab').click();
  cy.contains('Metrisch');
  cy.contains('Ordinal');
  cy.contains('Commit Zone');
  cy.contains('Target Zone');
  cy.contains('Stretch Goal');
  cy.contains('Owner');
  cy.contains('Beschreibung (optional)');
  cy.contains('Action Plan (optional)');
  cy.contains('Weitere Action hinzufügen');
  cy.contains('Speichern');
  cy.contains('Abbrechen');

  cy.fillOutKeyResult(
    title == null ? 'I am a ordinal keyresult' : title,
    null,
    null,
    null,
    'My commit zone',
    'My target zone',
    'My stretch goal',
    owner,
    'This is my description',
  );

  cy.getByTestId('submit').should('not.be.disabled');
  cy.getByTestId('submit').click();
});

Cypress.Commands.add(
  'createMetricKeyresult',
  (title: string | null, baseline: string | null, stretchGoal: string | null) => {
    cy.getByTestId('objective').first().getByTestId('add-keyResult').first().click();
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Key Result erfassen');
    cy.contains('Jaya Norris');
    cy.checkForDialogText();

    cy.fillOutKeyResult(
      title == null ? 'I am a metric keyresult' : title,
      'PERCENT',
      baseline === null ? '21' : baseline,
      stretchGoal === null ? '52' : stretchGoal,
      null,
      null,
      null,
      'Pac',
      'This is my description',
    );
    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('submit').click();
  },
);

Cypress.Commands.add('checkForDialogText', () => {
  cy.contains('Titel');
  cy.contains('Metrisch');
  cy.contains('Ordinal');
  cy.contains('Einheit');
  cy.contains('Baseline');
  cy.contains('Stretch Goal');
  cy.contains('Owner');
  cy.contains('Beschreibung (optional)');
  cy.contains('Action Plan (optional)');
  cy.contains('Weitere Action hinzufügen');
  cy.contains('Speichern');
  cy.contains('Abbrechen');
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
    owner: string | null,
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
    if (owner != null) {
      cy.getByTestId('ownerInput').clear().type(owner).type('{downarrow}').type('{enter}');
    }
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

function setConfidence(confidence: number) {
  cy.getByTestId('confidence-slider').find('input').focus();
  for (let i = 0; i < 10; i++) {
    cy.realPress('ArrowLeft');
  }
  for (let i = 0; i < confidence; i++) {
    cy.realPress('ArrowRight');
  }
}

function loginWithCredentials(username: string, password: string) {
  cy.intercept('GET', '/api/v1/users/current').as('getCurrentUser');
  cy.visit('/');
  cy.origin(Cypress.env('login_url'), { args: { username, password } }, ({ username, password }) => {
    cy.get('input[name="username"]').type(username);
    cy.get('input[name="password"]').type(password);
    cy.get('input[type="submit"]').click();
    cy.wait('@getCurrentUser');
  });
  cy.url().then((url) => {
    const currentUrl = new URL(url);
    const baseURL = new URL(Cypress.config().baseUrl!);
    expect(currentUrl.pathname).equal(baseURL.pathname);
  });
}

function checkForToaster(selector: string, amount: number, messages: string[] = []) {
  const toaster = cy.get('.ngx-toastr.' + selector);
  toaster.should('have.length', amount);
  if (amount == 0) return;
  toaster
    .find('.toast-message')
    .invoke('text')
    .then((e) => messages.forEach((m) => expect(e).contains(m)));
}

const overviewIsLoaded = () => cy.get('mat-chip').should('have.length.at.least', 2);

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
