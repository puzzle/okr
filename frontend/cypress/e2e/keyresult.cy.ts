import * as users from '../fixtures/users.json';

describe('OKR Overview', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  xit('Create new metric KeyResult', () => {
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
  });

  it('Create new ordinal KeyResult', () => {
    // cy.get('app-keyresult').should('have.length', 25);
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

    cy.getByTestId('titleInput').type('This is a ordinal KeyResult');

    cy.getByTestId('ordinalTab').click();
    cy.getByTestId('commitZone').type('My commit zone');
    cy.getByTestId('targetZone').type('My target zone');
    cy.getByTestId('stretchZone').type('My stretch zone');
    cy.getByTestId('ownerInput').type('Pac').type('{downarrow}').type('{enter}');
    cy.getByTestId('descriptionInput').type('This is my description');
    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('submit').click();

    // cy.get('app-keyresult').should('have.length', 26);

    cy.get('app-objective-column').first().get('app-keyresult').last().click();

    cy.contains('This is a ordinal KeyResult');
    cy.contains('Ordinal');
    cy.contains('Paco Eggimann');
    cy.contains('Fail');
    cy.contains('Commit');
    cy.contains('Target');
    cy.contains('My commit zone');
    cy.contains('My target zone');
    cy.contains('My stretch zone');
    cy.contains('Stretch');
    cy.contains('Confidence');
    cy.contains('Beschrieb');
    cy.contains('This is my description');
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
