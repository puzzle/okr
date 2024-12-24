import * as users from '../fixtures/users.json';
import { uniqueSuffix } from '../support/helper/utils';
import CyOverviewPage from '../support/helper/dom-helper/pages/overviewPage';
import { Unit } from '../../src/app/shared/types/enums/Unit';
import KeyResultDetailPage from '../support/helper/dom-helper/pages/keyResultDetailPage';
import CheckInDialog from '../support/helper/dom-helper/dialogs/checkInDialog';
import CheckInHistoryDialog from '../support/helper/dom-helper/dialogs/checkInHistoryDialog';
import ConfirmDialog from '../support/helper/dom-helper/dialogs/confirmDialog';

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
      .withMetricValues(Unit.PERCENT, '21', '51')
      .fillKeyResultDescription('This is my description')
      .submit();
    keyResultDetailPage
      .visit('Very important keyresult')
      .createCheckIn()
      .checkForDialogTextMetric()
      .fillMetricCheckInValue('30')
      .setCheckInConfidence(6)
      .fillCheckInCommentary('We bought a new house')
      .fillCheckInInitiatives('We have to buy more PCs')
      .submit();
    cy.contains('30%');
    cy.contains('6/10');
    cy.contains('Letztes Check-in (' + getCurrentDate() + ')');
    cy.contains('We bought a new house');
  });

  it('should create check-in metric with confidence 0', () => {
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('Very important keyresult')
      .withMetricValues(Unit.PERCENT, '21', '51')
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
      .withMetricValues(Unit.PERCENT, '21', '52')
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
      .selectOrdinalCheckInZone('commit')
      .setCheckInConfidence(6)
      .fillCheckInCommentary('There is a new car')
      .fillCheckInInitiatives('Buy a new pool')
      .submit();

    cy.contains('6/10');
    cy.contains('There is a new car');
    cy.contains('Letztes Check-in (' + getCurrentDate() + ')');
  });

  it('should generate check-in list', () => {
    overviewPage
      .addKeyResult()
      .fillKeyResultTitle('This will give a checkin list')
      .withMetricValues(Unit.PERCENT, '21', '52')
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
      .withMetricValues(Unit.CHF, '10', '300')
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
      .withMetricValues(Unit.EUR, '10', '300')
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
      .withMetricValues(Unit.FTE, '10', '300')
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
      .withMetricValues(Unit.PERCENT, '21', '52')
      .fillKeyResultDescription('This is my description')
      .submit();
    keyResultDetailPage.visit('I am a metric keyresult for testing');
    keyResultDetailPage.elements.addCheckin()
      .click();
    ConfirmDialog.do()
      .checkTitle('Check-in im Draft-Status');
    ConfirmDialog.do()
      .checkDescription('Dein Objective befindet sich noch im DRAFT Status. Möchtest du das Check-in trotzdem erfassen?');
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
      .withMetricValues(Unit.PERCENT, '45', '60')
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
});

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
