import * as users from '../fixtures/users.json';

describe('OKR Overview', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  xit('Create new metric KeyResult', () => {
    cy.getByTestId('add-keyResult').first().click();
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Key Result erfassen');
    checkForDialogText();

    cy.fillOutKeyResult(
      'I am a metric keyresult',
      'PERCENT',
      '21',
      '52',
      null,
      null,
      null,
      'Pac',
      'This is my description',
    );
    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('submit').click();
    cy.getByTestId('objective').first().get('app-keyresult:last').click();

    cy.contains('This is a metric KeyResult');
    cy.contains('Metrisch');
    cy.contains('Paco Eggimann');
    cy.contains('21');
    cy.contains('21');
    cy.contains('Stretch');
    cy.contains('Confidence');
    cy.contains('Beschrieb');
    cy.contains('This is my description');
  });

  xit('Create new ordinal KeyResult', () => {
    // cy.get('app-keyresult').should('have.length', 25);
    cy.getByTestId('add-keyResult').first().click();
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Key Result erfassen');
    checkForDialogText();
    cy.contains('Jaya Norris');
    cy.getByTestId('titleInput').type('Title');

    cy.getByTestId('ordinalTab').click();

    cy.fillOutKeyResult(
      'I am a ordinal keyresult',
      null,
      null,
      null,
      'My commit zone',
      'My target zone',
      'My stretch zone',
      'Pac',
      'This is my description',
    );

    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('submit').click();

    // cy.get('app-keyresult').should('have.length', 26);

    cy.getByTestId('objective').first().get('app-keyresult:last').click();

    cy.contains('This is a ordinal keyresult');
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

  xit('Create new KeyResult and Save and New', () => {
    cy.getByTestId('add-keyResult').first().click();
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Key Result erfassen');
    checkForDialogText();

    cy.fillOutKeyResult(
      'I am a metric keyresult with a new one',
      'PERCENT',
      '21',
      '52',
      null,
      null,
      null,
      'Pac',
      'This is my description when creating and then open a new',
    );
    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('saveAndNew').click();

    cy.contains('Key Result erfassen');
    cy.getByTestId('submit').should('be.disabled');
    checkForDialogText();
  });

  xit('Create and edit KeyResult with Action Plan', () => {
    cy.getByTestId('add-keyResult').first().click();
    cy.contains('Key Result erfassen');
    cy.getByTestId('titleInput').type('Title');

    cy.getByTestId('ordinalTab').click();
    cy.fillOutKeyResult(
      'I am a ordinal keyresult',
      null,
      null,
      null,
      'My commit zone',
      'My target zone',
      'My stretch zone',
      'Pac',
      'This is my description',
    );

    cy.getByTestId('submit').should('not.be.disabled');

    cy.getByTestId('actionInput').first().type('A new car');
    cy.getByTestId('actionInput').last().type('A new house');
    cy.getByTestId('add-action-plan-line').click();
    cy.getByTestId('actionInput').last().type('A new company');

    cy.getByTestId('actionInput').first().should('have.value', 'A new car');
    cy.getByTestId('actionInput').last().should('have.value', 'A new company');

    cy.getByTestId('submit').click();

    // KR detail should contain action plan

    // Edit should change row on kr detail
  });

  xit('Edit a KeyResult without type change', () => {
    cy.getByTestId('objective').first().get('app-keyresult:first').click();
    cy.getByTestId('edit-keyResult').click();
    cy.getByTestId('submit').should('not.be.disabled');

    cy.contains('Key Result bearbeiten');
    checkForDialogText();

    cy.getByTestId('titleInput').should(
      'have.value',
      'Prow scuttle parrel provost Sail ho shrouds spirits boom mizzenmast yardarm.',
    );
    cy.getByTestId('unit').should('have.value', 'PERCENT');
    cy.getByTestId('baseline').should('have.value', '21');
    cy.getByTestId('stretchGoal').should('have.value', '15052');
    cy.getByTestId('ownerInput').should('have.value', 'Paco Eggimann');
    cy.getByTestId('descriptionInput').should(
      'have.value',
      '' +
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lore',
    );

    cy.fillOutKeyResult(
      'This is the new title',
      'CHF',
      '22',
      '33',
      null,
      null,
      null,
      'Pac',
      'This is my new description',
    );
    cy.getByTestId('submit').click();

    cy.contains('This is the new title');
    cy.contains('22');
    cy.contains('33');
    cy.contains('Paco Eggimann');
    cy.contains('This is my new description');
  });

  xit('Edit a KeyResult with type change', () => {
    cy.getByTestId('objective').first().get('app-keyresult:first').click();
    cy.getByTestId('edit-keyResult').click();
    cy.getByTestId('submit').should('not.be.disabled');

    cy.contains('Key Result bearbeiten');

    checkForDialogText();

    cy.getByTestId('titleInput').should(
      'have.value',
      'Prow scuttle parrel provost Sail ho shrouds spirits boom mizzenmast yardarm.',
    );
    cy.getByTestId('unit').should('have.value', 'PERCENT');
    cy.getByTestId('baseline').should('have.value', '21');
    cy.getByTestId('stretchGoal').should('have.value', '15052');
    cy.getByTestId('ownerInput').should('have.value', 'Paco Eggimann');
    cy.getByTestId('descriptionInput').should(
      'have.value',
      '' +
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lore',
    );

    cy.getByTestId('titleInput').clear();
    cy.getByTestId('titleInput').type('This is the new title');
    cy.getByTestId('ordinalTab').click();
    cy.getByTestId('commitZone').type('My commit zone');
    cy.getByTestId('targetZone').type('My target zone');
    cy.getByTestId('stretchZone').type('My stretch zone');
    cy.getByTestId('ownerInput').clear();
    cy.getByTestId('ownerInput').type('Pac').type('{downarrow}').type('{enter}');
    cy.getByTestId('descriptionInput').clear();
    cy.getByTestId('descriptionInput').type('This is my new description');

    cy.getByTestId('submit').click();

    cy.contains('This is the new title');
    cy.contains('My commit zone');
    cy.contains('My target zone');
    cy.contains('My stretch zone');
    cy.contains('Paco Eggimann');
    cy.contains('This is my new description');
  });

  xit('Check validation in keyresult dialog', () => {
    cy.getByTestId('add-keyResult').first().click();
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Key Result erfassen');
    checkForDialogText();

    cy.fillOutKeyResult(
      'I am a metric keyresult',
      'PERCENT',
      '21',
      '52',
      null,
      null,
      null,
      'Pac',
      'This is my description',
    );
    cy.getByTestId('submit').should('not.be.disabled');

    cy.getByTestId('titleInput').clear();
    cy.getByTestId('submit').should('be.disabled');

    cy.getByTestId('titleInput').type('My title');
    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('baseline').clear();
    cy.getByTestId('submit').should('be.disabled');

    cy.getByTestId('baseline').type('abc');
    cy.getByTestId('submit').should('be.disabled');

    cy.getByTestId('baseline').clear();
    cy.getByTestId('baseline').type('45');
    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('stretchGoal').clear();
    cy.getByTestId('submit').should('be.disabled');

    cy.getByTestId('stretchGoal').type('abc');
    cy.getByTestId('submit').should('be.disabled');

    cy.getByTestId('stretchGoal').clear();
    cy.getByTestId('stretchGoal').type('83');
    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('ownerInput').clear();
    cy.getByTestId('submit').should('be.disabled');

    cy.getByTestId('ownerInput').type('abc');
    cy.getByTestId('submit').should('be.disabled');

    cy.getByTestId('ownerInput').clear();
    cy.getByTestId('ownerInput').type('Pac').type('{downarrow}').type('{enter}');
    cy.getByTestId('submit').should('not.be.disabled');

    cy.getByTestId('ordinalTab').click();
    cy.getByTestId('submit').should('be.disabled');

    cy.getByTestId('commitZone').clear().type('Commit');
    cy.getByTestId('targetZone').clear().type('Target');
    cy.getByTestId('stretchZone').clear().type('Stretch');
    cy.getByTestId('submit').should('not.be.disabled');

    cy.getByTestId('commitZone').clear();
    cy.getByTestId('submit').should('be.disabled');

    cy.getByTestId('commitZone').type('Commit');
    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('targetZone').clear();
    cy.getByTestId('submit').should('be.disabled');

    cy.getByTestId('targetZone').type('Target');
    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('stretchZone').clear();
    cy.getByTestId('submit').should('be.disabled');

    cy.getByTestId('stretchZone').type('Commit');
    cy.getByTestId('submit').should('not.be.disabled');
  });

  xit('Delete existing keyresult', () => {
    cy.get('app-keyresult').should('have.length', 30);

    cy.getByTestId('objective').first().get('app-keyresult:first').click();
    cy.getByTestId('edit-keyResult').click();

    checkForDialogText();

    cy.getByTestId('delete').click();
    cy.getByTestId('confirmYes').click();

    cy.contains('Puzzle ITC');
    cy.get('app-keyresult').should('have.length', 29);
  });
});

function checkForDialogText() {
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
}
