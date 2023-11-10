import * as users from '../fixtures/users.json';

describe('OKR Overview', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    cy.visit('/?quarter=2');
  });

  it('Create new metric KeyResult', () => {
    cy.createMetricKeyresult(null);
    cy.getByTestId('keyresult').contains('I am a metric keyresult').click();

    cy.contains('I am a metric keyresult');
    cy.contains('Metrisch');
    cy.contains('Paco Eggimann');
    cy.contains('21%');
    cy.contains('52%');
    cy.contains('Stretch');
    cy.contains('Confidence');
    cy.contains('Beschrieb');
    cy.contains('This is my description');
    cy.contains('Check-in erfassen');
    cy.contains('Key Result bearbeiten');
  });

  it('Create new ordinal KeyResult', () => {
    cy.createOrdinalKeyresult(null, 'Pac');

    cy.getByTestId('keyresult').contains('I am a ordinal keyresult').click();
    cy.contains('I am a ordinal keyresult');
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
    cy.contains('Check-in erfassen');
    cy.contains('Key Result bearbeiten');
  });

  it('Create new KeyResult and Save and New', () => {
    cy.getByTestId('objective').first().getByTestId('add-keyResult').first().click();
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Key Result erfassen');
    cy.contains('Jaya Norris');

    cy.fillOutKeyResult(
      'I am a metric keyresult with a new one',
      'PERCENT',
      '21',
      '52',
      null,
      null,
      null,
      null,
      'This is my description when creating and then open a new',
    );
    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('saveAndNew').click();

    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Key Result erfassen');
    cy.contains('Jaya Norris');
  });

  it('Create and edit KeyResult with Action Plan', () => {
    cy.getByTestId('objective').first().getByTestId('add-keyResult').first().click();
    cy.contains('Key Result erfassen');
    cy.contains('Jaya Norris');
    cy.getByTestId('titleInput').type('Title');

    cy.getByTestId('ordinalTab').click();
    cy.fillOutKeyResult(
      'This is a keyresult with an action plan',
      null,
      null,
      null,
      'My commit zone',
      'My target zone',
      'My stretch zone',
      null,
      'This is my description',
    );

    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('actionInput').should('have.length', 3);

    cy.getByTestId('actionInput').first().type('A new car');
    cy.getByTestId('actionInput').last().type('A new house');
    cy.getByTestId('add-action-plan-line').click();
    cy.getByTestId('actionInput').last().type('A new company');

    cy.getByTestId('actionInput').first().should('have.value', 'A new car');
    cy.getByTestId('actionInput').last().should('have.value', 'A new company');
    cy.getByTestId('actionInput').should('have.length', 4);

    cy.getByTestId('submit').click();

    cy.getByTestId('keyresult').contains('This is a keyresult with an action plan').click();

    cy.contains('This is a keyresult with an action plan');
    cy.contains('Ordinal');
    cy.contains('My commit zone');
    cy.contains('My target zone');
    cy.contains('My stretch zone');
    cy.contains('A new car');
    cy.contains('A new house');
    cy.contains('A new company');
    cy.getByTestId('edit-keyResult').click();

    cy.getByTestId('actionInput').should('have.length', 3);
  });

  it('Edit a KeyResult without type change', () => {
    cy.createOrdinalKeyresult('We want not to change keyresult title', null);

    cy.getByTestId('keyresult').contains('We want not to change keyresult title').last().click();
    cy.getByTestId('edit-keyResult').click();
    cy.getByTestId('submit').should('not.be.disabled');

    cy.contains('Key Result bearbeiten');

    cy.getByTestId('titleInput').should('have.value', 'We want not to change keyresult title');
    cy.getByTestId('commitZone').should('have.value', 'My commit zone');
    cy.getByTestId('targetZone').should('have.value', 'My target zone');
    cy.getByTestId('stretchZone').should('have.value', 'My stretch zone');
    cy.getByTestId('ownerInput').should('have.value', 'Jaya Norris');
    cy.getByTestId('descriptionInput').should('have.value', 'This is my description');

    cy.fillOutKeyResult(
      'This is the new title',
      null,
      null,
      null,
      'New commit',
      'New target',
      'New stretch',
      null,
      'This is my new description',
    );
    cy.getByTestId('submit').click();

    cy.contains('This is the new title');
    cy.contains('New commit');
    cy.contains('New target');
    cy.contains('New stretch');
    cy.contains('Jaya Norris');
    cy.contains('This is my new description');
  });

  it('Edit a KeyResult with type change', () => {
    cy.createOrdinalKeyresult('Here we want to change keyresult title', null);

    cy.getByTestId('keyresult').contains('Here we want to change keyresult title').last().click();
    cy.getByTestId('edit-keyResult').click();
    cy.getByTestId('submit').should('not.be.disabled');

    cy.contains('Key Result bearbeiten');

    cy.getByTestId('titleInput').should('have.value', 'Here we want to change keyresult title');
    cy.getByTestId('commitZone').should('have.value', 'My commit zone');
    cy.getByTestId('targetZone').should('have.value', 'My target zone');
    cy.getByTestId('stretchZone').should('have.value', 'My stretch zone');
    cy.getByTestId('ownerInput').should('have.value', 'Jaya Norris');
    cy.getByTestId('descriptionInput').should('have.value', 'This is my description');

    cy.getByTestId('metricTab').click();

    cy.fillOutKeyResult(
      'This is my new title for the new metric keyresult',
      'PERCENT',
      '21',
      '56',
      null,
      null,
      null,
      null,
      'This is my new description',
    );

    cy.getByTestId('submit').click();
    cy.getByTestId('keyresult').contains('This is my new title for the new metric keyresult').first().click();

    cy.contains('This is my new title for the new metric keyresult');
    cy.contains('21%');
    cy.contains('56%');
    cy.contains('Metrisch');
    cy.contains('Jaya Norris');
    cy.contains('This is my new description');
  });

  it('Check validation in keyresult dialog', () => {
    cy.getByTestId('objective').first().getByTestId('add-keyResult').first().click();
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Key Result erfassen');

    cy.fillOutKeyResult(
      'I am a metric keyresult',
      'PERCENT',
      '21',
      '52',
      null,
      null,
      null,
      null,
      'This is my description',
    );
    cy.getByTestId('submit').should('not.be.disabled');

    cy.getByTestId('titleInput').clear();
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Dieses Feld muss ausgefüllt sein');

    cy.getByTestId('titleInput').type('My title');
    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('baseline').clear();
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Dieses Feld muss ausgefüllt sein');

    cy.getByTestId('baseline').type('abc');
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Dieser Wert muss dem vorgegebenen Muster entsprechen');

    cy.getByTestId('baseline').clear();
    cy.getByTestId('baseline').type('45');
    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('stretchGoal').clear();
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Dieses Feld muss ausgefüllt sein');

    cy.getByTestId('stretchGoal').type('abc');
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Dieser Wert muss dem vorgegebenen Muster entsprechen');

    cy.getByTestId('stretchGoal').clear();
    cy.getByTestId('stretchGoal').type('83');
    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('ownerInput').clear();
    cy.getByTestId('submit').should('be.disabled');

    cy.getByTestId('ownerInput').type('abc');
    cy.getByTestId('titleInput').type('Hello');
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Du musst einen Owner auswählen');

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
    cy.contains('Dieses Feld muss ausgefüllt sein');

    cy.getByTestId('commitZone').type('Commit');
    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('targetZone').clear();
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Dieses Feld muss ausgefüllt sein');

    cy.getByTestId('targetZone').type('Target');
    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('stretchZone').clear();
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Dieses Feld muss ausgefüllt sein');

    cy.getByTestId('stretchZone').type('Commit');
    cy.getByTestId('submit').should('not.be.disabled');
  });

  it('Delete existing keyresult', () => {
    cy.createOrdinalKeyresult('A keyresult to delete', null);

    cy.getByTestId('keyresult').contains('A keyresult to delete').last().click();

    cy.getByTestId('edit-keyResult').click();

    cy.getByTestId('delete-keyResult').click();
    cy.getByTestId('confirmYes').click();

    cy.contains('Puzzle ITC');
    cy.get('A keyresult to delete').should('not.exist');
  });
});
