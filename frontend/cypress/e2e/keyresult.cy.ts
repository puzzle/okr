import * as users from '../fixtures/users.json';

describe('OKR Overview', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  it('Create new metric KeyResult', () => {
    cy.getByTestId('add-keyResult').first().click();
    cy.contains('Key Result erfassen');
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
    cy.contains('Speichern & Neu');
    cy.contains('Abbrechen');

    cy.getByTestId('titleInput').type('This is a title');
    cy.getByTestId('baseline').type('21');
    cy.getByTestId('stretchGoal').type('52');
    cy.getByTestId('ownerInput').type('Pac').type('{downarrow}').type('{enter}');
    cy.getByTestId('descriptionInput').type('This is my description');
    cy.getByTestId('submit').click();

    cy.wait(2000);
  });

  xit('Create new ordinal KeyResult', () => {
    cy.getByTestId('add-keyresult').first().click();
    cy.contains('Key Result erfassen');
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
    cy.contains('Speichern & Neu');
    cy.contains('Abbrechen');
  });

  xit('Create and open new KeyResult', () => {});

  xit('Create and edit KeyResult with Action Plan', () => {
    cy.getByTestId('add-keyresult').first().click();
    cy.contains('Key Result erfassen');

    cy.getByTestId('add-action-plan-line').click();

    // KR detail should contain action plan

    // Edit should change row on kr detail
  });

  xit('Edit a KeyResult', () => {
    cy.get('.action-email').should('have.value', 'fake@email.com');
  });
});
