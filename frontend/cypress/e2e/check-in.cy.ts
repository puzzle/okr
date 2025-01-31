import * as users from '../fixtures/users.json';
import { uniqueSuffix } from '../support/helper/utils';
import CyOverviewPage from '../support/helper/dom-helper/pages/overviewPage';
import { Unit } from '../../src/app/shared/types/enums/unit';
import KeyResultDetailPage from '../support/helper/dom-helper/pages/keyResultDetailPage';
import CheckInDialog from '../support/helper/dom-helper/dialogs/checkInDialog';
import CheckInHistoryDialog from '../support/helper/dom-helper/dialogs/checkInHistoryDialog';
import ConfirmDialog from '../support/helper/dom-helper/dialogs/confirmDialog';
import FilterHelper from '../support/helper/dom-helper/filterHelper';

describe('okr check-in', () => {
  let overviewPage = new CyOverviewPage();
  let keyResultDetailPage = new KeyResultDetailPage();

  beforeEach(() => {
    overviewPage = new CyOverviewPage();
    keyResultDetailPage = new KeyResultDetailPage();
    cy.loginAsUser(users.gl);
  });

  it('should create check-in metric', () => {
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('Very important keyresult')
      .withMetricValues(
        Unit.PERCENT, '21', undefined, '51'
      )
      .fillKeyResultDescription('This is my description')
      .submit();
    keyResultDetailPage
      .visit('Very important keyresult')
      .createCheckIn()
      .checkForDialogTextMetric()
      .addActionToActionPlan('A new action on the action-plan')
      .fillMetricCheckInValue('30')
      .setCheckInConfidence(6)
      .fillCheckInCommentary('We bought a new house')
      .fillCheckInInitiatives('We have to buy more PCs')
      .submit();
    cy.contains('30%');
    cy.contains('6/10');
    cy.contains('Letztes Check-in (' + getCurrentDate() + ')');
    cy.contains('We bought a new house');
    cy.contains('- A new action on the action-plan');
  });

  it('should show an error message when new check-in value is not numeric', () => {
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('This key-result will have errors')
      .withMetricValues(Unit.NUMBER, '21', '51')
      .submit();

    const detailPage = keyResultDetailPage
      .visit('Very important keyresult')
      .createCheckIn()
      .fillMetricCheckInValue('asdf');
    cy.contains('Neuer Wert muss eine Zahl sein.');

    detailPage.fillMetricCheckInValue('21. 2');
    cy.contains('Neuer Wert muss eine Zahl sein.');

    detailPage.fillMetricCheckInValue('123');
    cy.contains('Neuer Wert muss eine Zahl sein.')
      .should('not.exist');
  });


  it('should create check-in metric and assert correct owner', () => {
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('This keyresult is for the owner')
      .withMetricValues(
        Unit.PERCENT, '21', undefined, '51'
      )
      .fillKeyResultDescription('This is my description')
      .submit();

    overviewPage.checkForToaster('Das Key Result wurde erfolgreich erstellt', 'success');

    keyResultDetailPage
      .visit('This keyresult is for the owner')
      .createCheckIn()
      .checkForDialogTextMetric()
      .fillMetricCheckInValue('30')
      .setCheckInConfidence(6)
      .fillCheckInCommentary('We bought a new house')
      .fillCheckInInitiatives('We have to buy more PCs')
      .submit();

    keyResultDetailPage.checkForToaster('Das Check-in wurde erfolgreich erstellt', 'success')
      .showAllCheckIns()
      .checkOnDialog(() => cy.contains('Jaya Norris'))
      .cancel();

    cy.logout();
    cy.loginAsUser(users.bbt);

    FilterHelper.do()
      .toggleOption('Alle');

    keyResultDetailPage
      .visit('This keyresult is for the owner')
      .showAllCheckIns()
      .checkOnDialog(() => cy.contains('Jaya Norris'))
      .editLatestCheckIn()
      .checkForDialogTextMetric()
      .setCheckInConfidence(7)
      .submit();

    keyResultDetailPage.checkForToaster('Das Check-in wurde erfolgreich aktualisiert', 'success');

    CheckInHistoryDialog.do()
      .checkOnDialog(() => cy.contains('Ashleigh Russell'))
      .checkForAttribute('Confidence:', '7 / 10')
      .cancel();
  });

  it('should create check-in metric with confidence 0', () => {
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('Very important keyresult')
      .withMetricValues(
        Unit.PERCENT, '21', undefined, '51'
      )
      .fillKeyResultDescription('This is my description')
      .submit();
    keyResultDetailPage
      .visit('Very important keyresult')
      .createCheckIn()
      .fillMetricCheckInValue('30')
      .setCheckInConfidence(0)
      .fillCheckInCommentary('We bought a new house')
      .fillCheckInInitiatives('We have to buy more PCs')
      .submit();

    cy.contains('30%');
    cy.contains('6/10');
    cy.contains('Letztes Check-in (' + getCurrentDate() + ')');
    cy.contains('We bought a new house');
  });

  it('should create check-in metric with value below baseline', () => {
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('This will not be good')
      .withMetricValues(
        Unit.PERCENT, '21', undefined, '52'
      )
      .fillKeyResultDescription('This is my description')
      .submit();
    keyResultDetailPage
      .visit('This will not be good')
      .createCheckIn()
      .fillMetricCheckInValue('5')
      .setCheckInConfidence(5)
      .submit();

    cy.contains('5%');
    cy.contains('!');
    cy.contains('5/10');
    cy.contains('Letztes Check-in (' + getCurrentDate() + ')');
  });

  it('should create check-in ordinal', () => {
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('A new ordinal keyresult for our company')
      .withOrdinalValues('New house', 'New car', 'New pool')
      .fillKeyResultDescription('This is my description')
      .submit();
    keyResultDetailPage
      .visit('A new ordinal keyresult for our company')
      .createCheckIn()
      .checkForDialogTextOrdinal()
      .addActionToActionPlan('A new action on the action-plan')
      .selectOrdinalCheckInZone('commit')
      .setCheckInConfidence(6)
      .fillCheckInCommentary('There is a new car')
      .fillCheckInInitiatives('Buy a new pool')
      .submit();

    cy.contains('6/10');
    cy.contains('There is a new car');
    cy.contains('Letztes Check-in (' + getCurrentDate() + ')');
    cy.contains('- A new action on the action-plan');
  });

  it('should generate check-in list', () => {
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('This will give a checkin list')
      .withMetricValues(
        Unit.PERCENT, '21', undefined, '52'
      )
      .fillKeyResultDescription('This is my description')
      .submit();
    keyResultDetailPage
      .visit('This will give a checkin list')
      .createCheckIn()
      .fillMetricCheckInValue('30')
      .setCheckInConfidence(5)
      .fillCheckInCommentary('We bought a new house')
      .fillCheckInInitiatives('We have to buy more PCs')
      .submit();
    keyResultDetailPage
      .createCheckIn()
      .fillMetricCheckInValue('50')
      .setCheckInConfidence(6)
      .fillCheckInCommentary('This was a good idea')
      .fillCheckInInitiatives('Will be difficult')
      .submit();
    keyResultDetailPage
      .showAllCheckIns()
      .checkForAttribute('Confidence:', '5 / 10')
      .checkForAttribute('Confidence:', '6 / 10')
      .checkForAttribute('Veränderungen:', 'We bought a new house')
      .checkForAttribute('Veränderungen:', 'This was a good idea')
      .checkForAttribute('Massnahmen:', 'We have to buy more PCs')
      .checkForAttribute('Massnahmen:', 'Will be difficult');

    cy.contains('Check-in History');
    cy.contains(getCurrentDate());
    cy.contains('Wert: 30%');
    cy.contains('Wert: 50%');
  });

  it('should edit metric check-in', () => {
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('Here we edit a metric checkin')
      .withMetricValues(
        Unit.CHF, '10', undefined, '300'
      )
      .fillKeyResultDescription('This is my description')
      .submit();
    keyResultDetailPage
      .visit('Here we edit a metric checkin')
      .createCheckIn()
      .fillMetricCheckInValue('30')
      .setCheckInConfidence(5)
      .fillCheckInCommentary('Here we are')
      .fillCheckInInitiatives('A cat would be great')
      .submit();
    cy.contains('Aktuell: 30 CHF');
    keyResultDetailPage.showAllCheckIns();
    cy.contains('Check-in History');
    cy.contains('Wert: 30 CHF');
    CheckInHistoryDialog.do()
      .editLatestCheckIn();
    cy.contains('Here we edit a metric checkin');
    cy.contains('30 CHF');
    cy.contains('Confidence um Target Zone (213 CHF) zu erreichen');
    cy.contains('5/10');
    cy.contains('Here we are');
    cy.contains('A cat would be great');
    CheckInDialog.do()
      .fillMetricCheckInValue('200')
      .fillCheckInCommentary('We bought a new sheep')
      .submit();
    cy.contains('200 CHF');
    cy.contains('We bought a new sheep');
  });

  it('should generate correct labels in check-in history list', () => {
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('A new KeyResult for checking checkin list')
      .withMetricValues(
        Unit.EUR, '10', undefined, '300'
      )
      .fillKeyResultDescription('This is my description')
      .submit();
    keyResultDetailPage
      .visit('A new KeyResult for checking checkin list')
      .createCheckIn()
      .fillMetricCheckInValue('30')
      .setCheckInConfidence(5)
      .fillCheckInCommentary('Here we are')
      .fillCheckInInitiatives('A cat would be great')
      .submit();
    cy.contains('Aktuell: 30 EUR');
    keyResultDetailPage.showAllCheckIns();
    cy.contains('Check-in History');
    cy.contains('Wert: 30 EUR');
    CheckInHistoryDialog.do()
      .close();
    keyResultDetailPage.close();

    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('There is another kr with fte')
      .withMetricValues(
        Unit.FTE, '10', undefined, '300'
      )
      .fillKeyResultDescription('This is my description')
      .submit();
    keyResultDetailPage
      .visit('There is another kr with fte')
      .createCheckIn()
      .fillMetricCheckInValue('30')
      .setCheckInConfidence(5)
      .fillCheckInCommentary('Here we are')
      .fillCheckInInitiatives('A cat would be great')
      .submit();
    cy.contains('Aktuell: 30 FTE');
    keyResultDetailPage.showAllCheckIns();
    cy.contains('Check-in History');
    cy.contains('Wert: 30 FTE');
  });

  it('should edit ordinal check-in', () => {
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('For editing ordinal checkin')
      .withOrdinalValues('New house', 'New car', 'New pool')
      .fillKeyResultDescription('This is my description')
      .submit();
    keyResultDetailPage
      .visit('For editing ordinal checkin')
      .createCheckIn()
      .selectOrdinalCheckInZone('fail')
      .setCheckInConfidence(6)
      .fillCheckInCommentary('There is a new car')
      .fillCheckInInitiatives('Buy now a new pool')
      .submit();
    keyResultDetailPage.showAllCheckIns()
      .editLatestCheckIn();
    cy.contains('For editing ordinal checkin');
    cy.contains('Confidence um Target Zone zu erreichen');
    cy.contains('6/10');
    cy.contains('There is a new car');
    cy.contains('Buy now a new pool');
    CheckInDialog.do()
      .selectOrdinalCheckInZone('stretch')
      .fillCheckInCommentary('We bought a new dog')
      .submit();
    cy.contains('We bought a new dog');
    cy.contains('Buy now a new pool');
    cy.contains('STRETCH');
  });


  it('should display confirm dialog when creating check-in on draft objective', () => {
    overviewPage.addObjective()
      .fillObjectiveTitle('draft objective title')
      .selectQuarter('3')
      .submitDraftObjective();
    overviewPage.visitNextQuarter();
    overviewPage
      .addKeyResult(undefined, 'draft objective title')
      .fillKeyResultTitle('I am a metric keyresult for testing')
      .withMetricValues(
        Unit.PERCENT, '21', undefined, '52'
      )
      .fillKeyResultDescription('This is my description')
      .submit();
    keyResultDetailPage.visit('I am a metric keyresult for testing');
    keyResultDetailPage.elements.addCheckin()
      .click();
    ConfirmDialog.do()
      .checkForContentOnDialog('Check-in im Draft-Status');
    ConfirmDialog.do()
      .checkForContentOnDialog('Dein Objective befindet sich noch im DRAFT Status. Möchtest du das Check-in trotzdem erfassen?');
  });

  it('should only display last value div if last check-in is present', () => {
    const objectiveName = uniqueSuffix('new objective');

    overviewPage.addObjective()
      .fillObjectiveTitle(objectiveName)
      .selectQuarter('3')
      .submit();
    overviewPage.visitNextQuarter();
    overviewPage
      .addKeyResult(undefined, objectiveName)
      .fillKeyResultTitle('I am a keyresult metric')
      .withMetricValues(
        Unit.PERCENT, '45', undefined, '60'
      )
      .fillKeyResultDescription('Description')
      .submit();
    keyResultDetailPage.visit('I am a keyresult metric')
      .createCheckIn();
    cy.getByTestId('old-checkin-value')
      .should('not.exist');
    CheckInDialog.do()
      .fillMetricCheckInValue('10')
      .setCheckInConfidence(0)
      .fillCheckInCommentary('changeinfo')
      .fillCheckInInitiatives('initiatives')
      .submit();
    cy.contains(`Letztes Check-in (${getCurrentDate()})`);
    keyResultDetailPage.createCheckIn();
    cy.contains('Letzter Wert')
      .siblings('div')
      .contains('10%');
  });

  it.only('should be able to add actions to the action plan when creating a check-in', () => {
    const keyResultTitle = 'This key-result will be used for testing the action plan while creating check-ins';
    overviewPage.addKeyResult('Puzzle ITC', 'Wir wollen die Kundenzufriedenheit steigern')
      .fillKeyResultTitle(keyResultTitle)
      .withMetricValues(
        Unit.NUMBER, '1', undefined, '5'
      )
      .addActionPlanElement('First action')
      .addActionPlanElement('Second action')
      .addActionPlanElement('Third action')
      .submit();

    keyResultDetailPage.visit(keyResultTitle);
    keyResultDetailPage.editKeyResult();
    cy.getByTestId('action-input')
      .should('have.length', 3);
    cy.getByTestId('cancel')
      .click();

    cy.intercept('PUT', '**/action')
      .as('addAction');
    keyResultDetailPage.createCheckIn()
      .addActionToActionPlan('Fourth action')
      .fillCheckInCommentary('Add a fourth action to the action plan')
      .fillMetricCheckInValue('3')
      .submit();
    cy.contains('Fourth action');
    cy.wait('@addAction');

    keyResultDetailPage.editKeyResult();
    cy.getByTestId('action-input')
      .should('have.length', 4);
    cy.contains('Fourth action')
      .should('exist');
    cy.getByTestId('cancel')
      .click();
  });

  it('should be able to check actions of the action plan when creating a check-in', () => {
    keyResultDetailPage.visit('This key-result will be used for testing the action plan while creating check-ins');

    keyResultDetailPage.createCheckIn()
      .checkActionOfActionPlan(0)
      .fillCheckInCommentary('Check first action of action plan')
      .fillMetricCheckInValue('4')
      .submit();

    isChecked('First action');
    cy.contains('Second action')
      .should('exist');
    cy.contains('Third action')
      .should('exist');
    cy.contains('Fourth action')
      .should('exist');
  });

  it('should not save action plan changes when check-in is not saved', () => {
    keyResultDetailPage.visit('This key-result will be used for testing the action plan while creating check-ins');

    keyResultDetailPage.createCheckIn()
      .addActionToActionPlan('Fifth action!')
      .checkActionOfActionPlan(1)
      .fillCheckInCommentary('This should not appear anywhere!')
      .fillMetricCheckInValue('5')
      .cancel();

    cy.contains('First action')
      .should('exist');
    cy.contains('Second action')
      .should('exist');
    cy.contains('Third action')
      .should('exist');
    cy.contains('Fourth action')
      .should('exist');
    cy.contains('Fifth action!')
      .should('not.exist');

    isChecked('Second action', false);

    cy.contains('Check first action of action plan')
      .should('exist');
    cy.contains('This should not appear anywhere!')
      .should('not.exist');
  });

  it('should have a primary button on every check-in dialog', () => {
    keyResultDetailPage.visit('Very important keyresult')
      .createCheckIn()
      .run(cy.buttonShouldBePrimary('submit-check-in'))
      .cancel();

    keyResultDetailPage
      .showAllCheckIns()
      .run(cy.buttonShouldBePrimary('close-button'))
      .editLatestCheckIn()
      .run(cy.buttonShouldBePrimary('submit-check-in'));
  });
});

function isChecked(element: string, checked = true) {
  const assertion = checked ? 'include' : 'not.include';
  cy.contains(element)
    .should('have.css', 'text-decoration')
    .and(assertion, 'line-through');
}

function getCurrentDate() {
  const today = new Date();
  const yyyy = today.getFullYear();
  const mm = today.getMonth() + 1; // Months start at 0!
  const dd = today.getDate();

  let ddStr = '' + dd;
  let mmStr = '' + mm;
  if (dd < 10) {
    ddStr = '0' + ddStr;
  }
  if (mm < 10) {
    mmStr = '0' + mmStr;
  }

  return ddStr + '.' + mmStr + '.' + yyyy;
}
