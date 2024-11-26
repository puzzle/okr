import * as users from '../fixtures/users.json';
import CyOverviewPage from '../support/helper/dom-helper/pages/overviewPage';
import KeyResultDetailPage from '../support/helper/dom-helper/pages/keyResultDetailPage';
import { Unit } from '../../src/app/shared/types/enums/Unit';
import KeyResultDialog from '../support/helper/dom-helper/dialogs/keyResultDialog';

describe('OKR Overview', () => {
  let overviewPage = new CyOverviewPage();
  let keyResultDetailPage = new KeyResultDetailPage();

  beforeEach(() => {
    overviewPage = new CyOverviewPage();
    keyResultDetailPage = new KeyResultDetailPage();
    cy.loginAsUser(users.gl);
  });

  it('Create new metric KeyResult', () => {
    overviewPage
      .addKeyResult()
      .checkForDialogTextMetric()
      .fillKeyResultTitle('I am a metric keyresult')
      .withMetricValues(Unit.PERCENT, '21', '52')
      .fillOwner('Bob Baumeister')
      .fillKeyResultDescription('This is my description')
      .submit();
    keyResultDetailPage.visit('I am a metric keyresult');

    cy.contains('I am a metric keyresult');
    cy.contains('Metrisch');
    cy.contains('Bob Baumeister');
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
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('I am a ordinal keyresult')
      .withOrdinalValues('My commit zone', 'My target zone', 'My stretch goal')
      .checkForDialogTextOrdinal()
      .fillOwner('Bob Baumeister')
      .fillKeyResultDescription('This is my description')
      .submit();
    keyResultDetailPage.visit('I am a ordinal keyresult');

    cy.contains('I am a ordinal keyresult');
    cy.contains('Ordinal');
    cy.contains('Bob Baumeister');
    cy.contains('Fail');
    cy.contains('Commit');
    cy.contains('Target');
    cy.contains('My commit zone');
    cy.contains('My target zone');
    cy.contains('My stretch goal');
    cy.contains('Stretch');
    cy.contains('Confidence');
    cy.contains('Beschrieb');
    cy.contains('This is my description');
    cy.contains('Check-in erfassen');
    cy.contains('Key Result bearbeiten');
  });

  it('Create new KeyResult and Save and New', () => {
    overviewPage
      .addKeyResult()
      .checkForDialogTextMetric()
      .fillKeyResultTitle('I am a metric keyresult with a new one')
      .withMetricValues(Unit.PERCENT, '21', '52')
      .fillOwner('Bob Baumeister')
      .fillKeyResultDescription('This is my description when creating and then open a new')
      .saveAndNew();
    cy.contains('Jaya Norris');
    KeyResultDialog.do().checkForDialogTextMetric();
  });

  it('Create and edit KeyResult with Action Plan', () => {
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('This is a keyresult with an action plan')
      .withOrdinalValues('My commit zone', 'My target zone', 'My stretch goal')
      .fillOwner('Bob Baumeister')
      .fillKeyResultDescription('This is my description')
      .addActionPlanElement('A new car')
      .addActionPlanElement('A new house')
      .addActionPlanElement('A new company')
      .submit();

    keyResultDetailPage.visit('This is a keyresult with an action plan');

    cy.contains('This is a keyresult with an action plan');
    cy.contains('Ordinal');
    cy.contains('My commit zone');
    cy.contains('My target zone');
    cy.contains('My stretch goal');
    cy.contains('A new car');
    cy.contains('A new house');
    cy.contains('A new company');

    keyResultDetailPage.editKeyResult();
    cy.getByTestId('actionInput').should('have.length', 3);
  });

  it('Edit a KeyResult without type change', () => {
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('We want not to change keyresult title')
      .withOrdinalValues('My commit zone', 'My target zone', 'My stretch goal')
      .checkForDialogTextOrdinal()
      .fillKeyResultDescription('This is my description')
      .submit();
    keyResultDetailPage.visit('We want not to change keyresult title').editKeyResult();

    cy.getByTestId('submit').should('not.be.disabled');
    cy.contains('Key Result bearbeiten');
    cy.getByTestId('titleInput').should('have.value', 'We want not to change keyresult title');
    cy.getByTestId('commitZone').should('have.value', 'My commit zone');
    cy.getByTestId('targetZone').should('have.value', 'My target zone');
    cy.getByTestId('stretchZone').should('have.value', 'My stretch goal');
    cy.getByTestId('ownerInput').should('have.value', 'Jaya Norris');
    cy.getByTestId('descriptionInput').should('have.value', 'This is my description');

    KeyResultDialog.do()
      .fillKeyResultTitle('This is the new title')
      .withOrdinalValues('New commit', 'New target', 'New stretch')
      .fillKeyResultDescription('This is my new description')
      .submit();

    cy.contains('This is the new title');
    cy.contains('New commit');
    cy.contains('New target');
    cy.contains('New stretch');
    cy.contains('Jaya Norris');
    cy.contains('This is my new description');
  });

  it('Edit a KeyResult with type change', () => {
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('Here we want to change keyresult title')
      .withOrdinalValues('My commit zone', 'My target zone', 'My stretch goal')
      .checkForDialogTextOrdinal()
      .fillKeyResultDescription('This is my description')
      .submit();
    keyResultDetailPage.visit('Here we want to change keyresult title').editKeyResult();

    cy.getByTestId('submit').should('not.be.disabled');
    cy.contains('Key Result bearbeiten');
    cy.getByTestId('titleInput').should('have.value', 'Here we want to change keyresult title');
    cy.getByTestId('commitZone').should('have.value', 'My commit zone');
    cy.getByTestId('targetZone').should('have.value', 'My target zone');
    cy.getByTestId('stretchZone').should('have.value', 'My stretch goal');
    cy.getByTestId('ownerInput').should('have.value', 'Jaya Norris');
    cy.getByTestId('descriptionInput').should('have.value', 'This is my description');

    KeyResultDialog.do()
      .fillKeyResultTitle('This is my new title for the new metric keyresult')
      .withMetricValues(Unit.PERCENT, '21', '56')
      .fillKeyResultDescription('This is my new description')
      .submit();

    cy.contains('This is my new title for the new metric keyresult');
    cy.contains('21%');
    cy.contains('56%');
    cy.contains('Metrisch');
    cy.contains('Jaya Norris');
    cy.contains('This is my new description');
  });

  it('Check validation in keyresult dialog', () => {
    overviewPage.addKeyResult().checkForDialogTextMetric();
    cy.getByTestId('submit').should('be.disabled');
    KeyResultDialog.do()
      .fillKeyResultTitle('I am a metric keyresult')
      .withMetricValues(Unit.PERCENT, '21', '52')
      .fillKeyResultDescription('This is my description');

    cy.getByTestId('submit').should('not.be.disabled');

    cy.getByTestId('titleInput').clear();
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Titel muss folgende Länge haben: 2-250 Zeichen');

    KeyResultDialog.do().fillKeyResultTitle('My title');
    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('baseline').clear();
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Baseline muss eine Zahl sein');

    KeyResultDialog.do().withMetricValues(Unit.PERCENT, 'abc', '52');
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Baseline muss eine Zahl sein');

    KeyResultDialog.do().withMetricValues(Unit.PERCENT, '45', '52');
    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('stretchGoal').clear();
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Stretch Goal muss eine Zahl sein');

    KeyResultDialog.do().withMetricValues(Unit.PERCENT, '45', 'abc');
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Stretch Goal muss eine Zahl sein');

    KeyResultDialog.do().withMetricValues(Unit.PERCENT, '45', '83');
    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('ownerInput').clear();
    cy.getByTestId('submit').should('be.disabled');

    cy.getByTestId('ownerInput').type('abc');
    cy.getByTestId('titleInput').type('Hello');
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Owner muss ausgewählt sein');

    KeyResultDialog.do().fillOwner('Bob Baumeister');
    cy.getByTestId('submit').should('not.be.disabled');

    cy.getByTestId('ordinalTab').click();
    cy.getByTestId('submit').should('be.disabled');

    KeyResultDialog.do().withOrdinalValues('Commit', 'Target', 'Stretch');
    cy.getByTestId('submit').should('not.be.disabled');

    cy.getByTestId('commitZone').clear();
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Commit Zone muss folgende Länge haben: 1-400 Zeichen');

    KeyResultDialog.do().withOrdinalValues('Commit', 'Target', 'Stretch');
    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('targetZone').clear();
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Target Zone muss folgende Länge haben: 1-400 Zeichen');

    KeyResultDialog.do().withOrdinalValues('Commit', 'Target', 'Stretch');
    cy.getByTestId('submit').should('not.be.disabled');
    cy.getByTestId('stretchZone').clear();
    cy.getByTestId('submit').should('be.disabled');
    cy.contains('Stretch Zone muss folgende Länge haben: 1-400 Zeichen');

    KeyResultDialog.do().withOrdinalValues('Commit', 'Target', 'Stretch');
    cy.getByTestId('submit').should('not.be.disabled');
  });

  it('Delete existing keyresult', () => {
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('A keyresult to delete')
      .withOrdinalValues('My commit zone', 'My target zone', 'My stretch goal')
      .checkForDialogTextOrdinal()
      .fillKeyResultDescription('This is my description')
      .submit();
    keyResultDetailPage
      .visit('A keyresult to delete')
      .editKeyResult()
      .deleteKeyResult()
      .checkTitle('Key Result löschen')
      .checkDescription(
        'Möchtest du dieses Key Result wirklich löschen? Zugehörige Check-ins werden dadurch ebenfalls gelöscht!',
      )
      .submit();

    cy.contains('Puzzle ITC');
    cy.get('A keyresult to delete').should('not.exist');
  });
});