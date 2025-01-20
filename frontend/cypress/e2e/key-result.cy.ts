import * as users from '../fixtures/users.json';
import CyOverviewPage from '../support/helper/dom-helper/pages/overviewPage';
import KeyResultDetailPage from '../support/helper/dom-helper/pages/keyResultDetailPage';
import { Unit } from '../../src/app/shared/types/enums/unit';
import KeyResultDialog from '../support/helper/dom-helper/dialogs/keyResultDialog';

describe('okr key-result', () => {
  let overviewPage = new CyOverviewPage();
  let keyResultDetailPage = new KeyResultDetailPage();

  beforeEach(() => {
    overviewPage = new CyOverviewPage();
    keyResultDetailPage = new KeyResultDetailPage();
    cy.loginAsUser(users.gl);
  });

  it('should create new metric key-result', () => {
    const keyResultDialog = overviewPage
      .addKeyResult()
      .checkForDialogTextMetric()
      .fillKeyResultTitle('I am a metric keyresult')
      .fillOwner('Bob Baumeister')
      .fillKeyResultDescription('This is my description');

    keyResultDialog.withMetricValues(
      Unit.PERCENT, '999999999999999999999999999999', '7', undefined
    );
    keyResultDialog.checkOnDialog(() => cy.contains('Baseline darf maximal 20 Zeichen lang sein.'));

    keyResultDialog.withMetricValues(
      Unit.PERCENT, '0', '7', undefined
    );
    cy.getByTestId('stretch-goal')
      .should('have.value', '10');

    keyResultDialog.withMetricValues(
      Unit.PERCENT, '0', undefined, '100'
    );
    cy.getByTestId('target-goal')
      .should('have.value', '70');

    keyResultDialog
      .withMetricValues(
        Unit.PERCENT, '21', undefined, '52'
      )
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

  it('should create new ordinal key-result', () => {
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

  it('should create new key-result and save and new', () => {
    overviewPage
      .addKeyResult()
      .checkForDialogTextMetric()
      .fillKeyResultTitle('I am a metric keyresult with a new one')
      .withMetricValues(
        Unit.PERCENT, '21', undefined, '52'
      )
      .fillOwner('Bob Baumeister')
      .fillKeyResultDescription('This is my description when creating and then open a new')
      .saveAndNew();
    cy.contains('Jaya Norris');
    KeyResultDialog.do()
      .checkForDialogTextMetric();
  });

  it('should create and edit key-result with action plan', () => {
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
    cy.getByTestId('action-input')
      .should('have.length', 3);
  });

  it('should edit an ordinal key-result without type change', () => {
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('We want not to change keyresult title')
      .withOrdinalValues('My commit zone', 'My target zone', 'My stretch goal')
      .checkForDialogTextOrdinal()
      .fillKeyResultDescription('This is my description')
      .submit();
    keyResultDetailPage.visit('We want not to change keyresult title')
      .editKeyResult();

    cy.getByTestId('submit')
      .should('not.be.disabled');
    cy.contains('Key Result bearbeiten');
    cy.getByTestId('title-input')
      .should('have.value', 'We want not to change keyresult title');
    cy.getByTestId('commit-zone')
      .should('have.value', 'My commit zone');
    cy.getByTestId('target-zone')
      .should('have.value', 'My target zone');
    cy.getByTestId('stretch-zone')
      .should('have.value', 'My stretch goal');
    cy.getByTestId('owner-input')
      .should('have.value', 'Jaya Norris');
    cy.getByTestId('description-input')
      .should('have.value', 'This is my description');

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

  it('should edit a metric key-result without type change', () => {
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('We want not to change metric keyresult title')
      .withMetricValues(
        Unit.PERCENT, '0', undefined, '10'
      )
      .checkForDialogTextMetric()
      .fillKeyResultDescription('This is my description')
      .submit();
    keyResultDetailPage.visit('We want not to change metric keyresult title')
      .editKeyResult();

    KeyResultDialog.do()
      .fillKeyResultTitle('This is the new title')
      .checkOnDialog(() => cy.getByTestId('baseline')
        .should('have.value', '0'))
      .checkOnDialog(() => cy.getByTestId('target-goal')
        .should('have.value', '7'))
      .checkOnDialog(() => cy.getByTestId('stretch-goal')
        .should('have.value', '10'))
      .withMetricValues(
        Unit.PERCENT, '0', '70', undefined
      )
      .run(cy.getByTestId('ordinal-tab')
        .click())
      .run(cy.getByTestId('metric-tab')
        .click())
      .checkOnDialog(() => cy.getByTestId('baseline')
        .should('have.value', '0'))
      .checkOnDialog(() => cy.getByTestId('target-goal')
        .should('have.value', '70'))
      .checkOnDialog(() => cy.getByTestId('stretch-goal')
        .should('have.value', '100'))
      .withMetricValues(
        Unit.PERCENT, '5', '8.5', undefined
      )
      .checkOnDialog(() => cy.getByTestId('stretch-goal')
        .should('have.value', '10'))
      .fillKeyResultDescription('This is my new description')
      .submit();

    cy.contains('This is the new title');
    cy.contains('Jaya Norris');
    cy.contains('This is my new description');
  });

  it('should edit a key-result with type change', () => {
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('Here we want to change keyresult title')
      .withOrdinalValues('My commit zone', 'My target zone', 'My stretch goal')
      .checkForDialogTextOrdinal()
      .fillKeyResultDescription('This is my description')
      .addActionPlanElement('Action 1')
      .addActionPlanElement('Action 2')
      .submit();
    keyResultDetailPage.visit('Here we want to change keyresult title')
      .editKeyResult();

    cy.getByTestId('submit')
      .should('not.be.disabled');
    cy.contains('Key Result bearbeiten');
    cy.getByTestId('title-input')
      .should('have.value', 'Here we want to change keyresult title');
    cy.getByTestId('commit-zone')
      .should('have.value', 'My commit zone');
    cy.getByTestId('target-zone')
      .should('have.value', 'My target zone');
    cy.getByTestId('stretch-zone')
      .should('have.value', 'My stretch goal');
    cy.getByTestId('owner-input')
      .should('have.value', 'Jaya Norris');
    cy.getByTestId('description-input')
      .should('have.value', 'This is my description');

    KeyResultDialog.do()
      .fillKeyResultTitle('This is my new title for the new metric keyresult')
      .withMetricValues(
        Unit.PERCENT, '21', undefined, '56'
      )
      .fillKeyResultDescription('This is my new description')
      .submit();

    cy.contains('This is my new title for the new metric keyresult');
    cy.contains('21%');
    cy.contains('56%');
    cy.contains('Metrisch');
    cy.contains('Jaya Norris');
    cy.contains('This is my new description');
    cy.contains('Action 1');
    cy.contains('Action 2');
  });

  it('A KeyResult should not be able to change type after a checkin', () => {
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('Here we want to create a checkin')
      .withOrdinalValues('My commit zone', 'My target zone', 'My stretch goal')
      .checkForDialogTextOrdinal()
      .fillKeyResultDescription('This is my description')
      .addActionPlanElement('Action 1')
      .addActionPlanElement('Action 2')
      .submit();

    keyResultDetailPage
      .visit('Here we want to create a checkin')
      .createCheckIn()
      .selectOrdinalCheckInZone('commit')
      .setCheckInConfidence(6)
      .submit();

    keyResultDetailPage.close();

    keyResultDetailPage.visit('Here we want to create a checkin')
      .editKeyResult();

    cy.getByTestId('metric-tab')
      .should('have.class', 'non-active');
  });

  it('should check validation in key-result dialog', () => {
    overviewPage.addKeyResult()
      .checkForDialogTextMetric();
    cy.getByTestId('submit')
      .should('be.disabled');
    KeyResultDialog.do()
      .fillKeyResultTitle('I am a metric keyresult');

    cy.getByTestId('submit')
      .should('not.be.disabled');

    cy.getByTestId('title-input')
      .clear();
    cy.getByTestId('submit')
      .should('be.disabled');
    cy.contains('Titel ist ein Pflichtfeld.');


    KeyResultDialog.do()
      .fillKeyResultTitle('a');
    cy.getByTestId('submit')
      .should('be.disabled');
    cy.contains('Titel muss mindestens 2 Zeichen lang sein.');


    KeyResultDialog.do()
      .fillKeyResultTitle('My title');
    cy.getByTestId('submit')
      .should('not.be.disabled');
    cy.getByTestId('baseline')
      .clear();
    cy.getByTestId('submit')
      .should('be.disabled');
    cy.contains('Baseline ist ein Pflichtfeld.');

    KeyResultDialog.do()
      .fillKeyResultTitle('My title')
      .withMetricValues(
        Unit.CHF, 'abc', undefined, '123'
      );
    cy.getByTestId('submit')
      .should('be.disabled');
    cy.contains('Baseline muss eine Zahl sein.');

    KeyResultDialog.do()
      .withMetricValues(
        Unit.PERCENT, '45', undefined, '52'
      );
    cy.getByTestId('submit')
      .should('not.be.disabled');
    cy.getByTestId('stretch-goal')
      .clear();
    cy.getByTestId('submit')
      .should('be.disabled');
    cy.contains('Stretch Goal ist ein Pflichtfeld.');

    KeyResultDialog.do()
      .withMetricValues(
        Unit.PERCENT, '45', undefined, 'abc'
      );
    cy.getByTestId('submit')
      .should('be.disabled');
    cy.contains('Stretch Goal muss eine Zahl sein.');

    KeyResultDialog.do()
      .withMetricValues(
        Unit.PERCENT, '45', undefined, '83'
      );
    cy.getByTestId('submit')
      .should('not.be.disabled');
    cy.getByTestId('owner-input')
      .clear();
    cy.getByTestId('submit')
      .should('be.disabled');

    cy.getByTestId('owner-input')
      .type('abc');
    cy.getByTestId('title-input')
      .type('Hello');
    cy.getByTestId('submit')
      .should('be.disabled');
    cy.contains('Owner muss ausgewählt sein');

    KeyResultDialog.do()
      .fillOwner('Bob Baumeister');
    cy.getByTestId('submit')
      .should('not.be.disabled');

    cy.getByTestId('ordinal-tab')
      .click();
    cy.getByTestId('submit')
      .should('be.disabled');

    KeyResultDialog.do()
      .withOrdinalValues('Commit', 'Target', 'Stretch');
    cy.getByTestId('submit')
      .should('not.be.disabled');

    cy.getByTestId('commit-zone')
      .clear();
    cy.getByTestId('submit')
      .should('be.disabled');
    cy.contains('Commit Zone ist ein Pflichtfeld.');
    KeyResultDialog.do()
      .withOrdinalValues('a', 'Target', 'Stretch');
    cy.getByTestId('submit')
      .should('be.disabled');
    cy.contains('Commit Zone muss mindestens 2 Zeichen lang sein.');

    KeyResultDialog.do()
      .withOrdinalValues('Commit', 'Target', 'Stretch');
    cy.getByTestId('submit')
      .should('not.be.disabled');
    cy.getByTestId('target-zone')
      .clear();
    cy.getByTestId('submit')
      .should('be.disabled');
    cy.contains('Target Zone ist ein Pflichtfeld.');
    KeyResultDialog.do()
      .withOrdinalValues('Commit', 'b', 'Stretch');
    cy.getByTestId('submit')
      .should('be.disabled');
    cy.contains('Target Zone muss mindestens 2 Zeichen lang sein.');

    KeyResultDialog.do()
      .withOrdinalValues('Commit', 'Target', 'Stretch');
    cy.getByTestId('submit')
      .should('not.be.disabled');
    cy.getByTestId('stretch-zone')
      .clear();
    cy.getByTestId('submit')
      .should('be.disabled');
    cy.contains('Stretch Zone ist ein Pflichtfeld.');
    KeyResultDialog.do()
      .withOrdinalValues('Commit', 'Target', 'c');
    cy.getByTestId('submit')
      .should('be.disabled');
    cy.contains('Stretch Zone muss mindestens 2 Zeichen lang sein.');
  });

  it('should delete existing key-result', () => {
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
      .checkForContentOnDialog('Key Result löschen')
      .checkForContentOnDialog('Möchtest du dieses Key Result wirklich löschen? Zugehörige Check-ins werden dadurch ebenfalls gelöscht!')
      .submit();

    cy.contains('Puzzle ITC');
    cy.get('A keyresult to delete')
      .should('not.exist');
  });

  it('should have primary button on all keyResult dialogs', () => {
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('A keyResult for testing purposes')
      .withOrdinalValues('My commit zone', 'My target zone', 'My stretch goal')
      .checkForPrimaryButton()
      .submit();

    keyResultDetailPage
      .visit('A keyResult for testing purposes')
      .editKeyResult()
      .checkForPrimaryButton()
      .cancel();
  });
});
