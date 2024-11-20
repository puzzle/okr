import * as users from '../fixtures/users.json';
import { onlyOn } from '@cypress/skip-test';
import { uniqueSuffix } from '../support/helper/utils';
import CyOverviewPage from '../support/helper/pom-helper/pages/overviewPage';
import { Unit } from '../../src/app/shared/types/enums/Unit';
import KeyResultDetailPage from '../support/helper/pom-helper/pages/keyResultDetailPage';
import CheckInDialog from '../support/helper/pom-helper/dialogs/checkInDialog';
import CheckInHistoryDialog from '../support/helper/pom-helper/dialogs/checkInHistoryDialog';
import ConfirmDialog from '../support/helper/pom-helper/dialogs/confirmDialog';

describe('OKR Check-in e2e tests', () => {
  describe('tests via click', () => {
    let op = new CyOverviewPage();
    let keyresultDetailPage = new KeyResultDetailPage();

    beforeEach(() => {
      op = new CyOverviewPage();
      keyresultDetailPage = new KeyResultDetailPage();
      cy.loginAsUser(users.gl);
      onlyOn('chrome');
    });

    it(`Create checkin metric`, () => {
      op.addKeyResult()
        .fillKeyResultTitle('Very important keyresult')
        .withMetricValues(Unit.PERCENT, '21', '51')
        .fillKeyResultDescription('This is my description')
        .submit();
      keyresultDetailPage
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

    it(`Create checkin metric with confidence 0`, () => {
      op.addKeyResult()
        .fillKeyResultTitle('Very important keyresult')
        .withMetricValues(Unit.PERCENT, '21', '51')
        .fillKeyResultDescription('This is my description')
        .submit();
      keyresultDetailPage
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

    it(`Create checkin metric with value below baseline`, () => {
      op.addKeyResult()
        .fillKeyResultTitle('This will not be good')
        .withMetricValues(Unit.PERCENT, '21', '52')
        .fillKeyResultDescription('This is my description')
        .submit();
      keyresultDetailPage
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

    it('Create checkin ordinal', () => {
      op.addKeyResult()
        .fillKeyResultTitle('A new ordinal keyresult for our company')
        .withOrdinalValues('New house', 'New car', 'New pool')
        .fillKeyResultDescription('This is my description')
        .submit();
      keyresultDetailPage
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

    it('Should generate checkin list', () => {
      op.addKeyResult()
        .fillKeyResultTitle('This will give a checkin list')
        .withMetricValues(Unit.PERCENT, '21', '52')
        .fillKeyResultDescription('This is my description')
        .submit();
      keyresultDetailPage
        .visit('This will give a checkin list')
        .createCheckIn()
        .fillMetricCheckInValue('30')
        .setCheckInConfidence(5)
        .fillCheckInCommentary('We bought a new house')
        .fillCheckInInitiatives('We have to buy more PCs')
        .submit();
      keyresultDetailPage
        .createCheckIn()
        .fillMetricCheckInValue('50')
        .setCheckInConfidence(6)
        .fillCheckInCommentary('This was a good idea')
        .fillCheckInInitiatives('Will be difficult')
        .submit();
      keyresultDetailPage.showAllCheckins();

      cy.contains('Check-in History');
      cy.contains(getCurrentDate());
      cy.contains('Wert: 30%');
      cy.contains('Wert: 50%');
      checkForAttribute('Confidence:', '5 / 10');
      checkForAttribute('Confidence:', '6 / 10');
      checkForAttribute('Veränderungen:', 'We bought a new house');
      checkForAttribute('Veränderungen:', 'This was a good idea');
      checkForAttribute('Massnahmen:', 'We have to buy more PCs');
      checkForAttribute('Massnahmen:', 'Will be difficult');
    });

    it('Edit metric checkin', () => {
      op.addKeyResult()
        .fillKeyResultTitle('Here we edit a metric checkin')
        .withMetricValues(Unit.CHF, '10', '300')
        .fillKeyResultDescription('This is my description')
        .submit();
      keyresultDetailPage
        .visit('Here we edit a metric checkin')
        .createCheckIn()
        .fillMetricCheckInValue('30')
        .setCheckInConfidence(5)
        .fillCheckInCommentary('Here we are')
        .fillCheckInInitiatives('A cat would be great')
        .submit();
      cy.contains('Aktuell: 30 CHF');
      keyresultDetailPage.showAllCheckins();
      cy.contains('Check-in History');
      cy.contains('Wert: 30 CHF');
      CheckInHistoryDialog.do().editLatestCheckIn();
      cy.contains('Here we edit a metric checkin');
      cy.contains('30 CHF');
      cy.contains('Confidence um Target Zone (213 CHF) zu erreichen');
      cy.contains('5/10');
      cy.contains('Here we are');
      cy.contains('A cat would be great');
      CheckInDialog.do().fillMetricCheckInValue('200').fillCheckInCommentary('We bought a new sheep').submit();
      cy.contains('200 CHF');
      cy.contains('We bought a new sheep');
    });

    it('Should generate right labels in checkin history list', () => {
      op.addKeyResult()
        .fillKeyResultTitle('A new KeyResult for checking checkin list')
        .withMetricValues(Unit.EUR, '10', '300')
        .fillKeyResultDescription('This is my description')
        .submit();
      keyresultDetailPage
        .visit('A new KeyResult for checking checkin list')
        .createCheckIn()
        .fillMetricCheckInValue('30')
        .setCheckInConfidence(5)
        .fillCheckInCommentary('Here we are')
        .fillCheckInInitiatives('A cat would be great')
        .submit();
      cy.contains('Aktuell: 30 EUR');
      keyresultDetailPage.showAllCheckins();
      cy.contains('Check-in History');
      cy.contains('Wert: 30 EUR');
      CheckInHistoryDialog.do().close();
      keyresultDetailPage.close();

      op.addKeyResult()
        .fillKeyResultTitle('There is another kr with fte')
        .withMetricValues(Unit.FTE, '10', '300')
        .fillKeyResultDescription('This is my description')
        .submit();
      keyresultDetailPage
        .visit('There is another kr with fte')
        .createCheckIn()
        .fillMetricCheckInValue('30')
        .setCheckInConfidence(5)
        .fillCheckInCommentary('Here we are')
        .fillCheckInInitiatives('A cat would be great')
        .submit();
      cy.contains('Aktuell: 30 FTE');
      keyresultDetailPage.showAllCheckins();
      cy.contains('Check-in History');
      cy.contains('Wert: 30 FTE');
    });

    it('Edit ordinal checkin', () => {
      op.addKeyResult()
        .fillKeyResultTitle('For editing ordinal checkin')
        .withOrdinalValues('New house', 'New car', 'New pool')
        .fillKeyResultDescription('This is my description')
        .submit();
      keyresultDetailPage
        .visit('For editing ordinal checkin')
        .createCheckIn()
        .selectOrdinalCheckInZone('fail')
        .setCheckInConfidence(6)
        .fillCheckInCommentary('There is a new car')
        .fillCheckInInitiatives('Buy now a new pool')
        .submit();
      keyresultDetailPage.showAllCheckins().editLatestCheckIn();
      cy.contains('For editing ordinal checkin');
      cy.contains('Confidence um Target Zone zu erreichen');
      cy.contains('6/10');
      cy.contains('There is a new car');
      cy.contains('Buy now a new pool');
      CheckInDialog.do().selectOrdinalCheckInZone('stretch').fillCheckInCommentary('We bought a new dog').submit();
      cy.contains('We bought a new dog');
      cy.contains('Buy now a new pool');
      cy.contains('STRETCH');
    });

    it(`Should display confirm dialog when creating checkin on draft objective`, () => {
      op.addObjective().fillObjectiveTitle('draft objective title').selectQuarter('3').submitDraftObjective();
      op.visitNextQuarter();
      op.addKeyResult(undefined, 'draft objective title')
        .fillKeyResultTitle('I am a metric keyresult for testing')
        .withMetricValues(Unit.PERCENT, '21', '52')
        .fillKeyResultDescription('This is my description')
        .submit();
      keyresultDetailPage.visit('I am a metric keyresult for testing');
      keyresultDetailPage.elements.addCheckin().click();
      ConfirmDialog.do().checkTitle('Check-in im Draft-Status');
      ConfirmDialog.do().checkDescription(
        'Dein Objective befindet sich noch im DRAFT Status. Möchtest du das Check-in trotzdem erfassen?',
      );
    });

    it(`Should only display last value div if last checkin is present`, () => {
      const objectiveName = uniqueSuffix('new objective');

      op.addObjective().fillObjectiveTitle(objectiveName).selectQuarter('3').submit();
      op.visitNextQuarter();
      op.addKeyResult(undefined, objectiveName)
        .fillKeyResultTitle('I am a keyresult metric')
        .withMetricValues(Unit.PERCENT, '45', '60')
        .fillKeyResultDescription('Description')
        .submit();
      keyresultDetailPage.visit('I am a keyresult metric').createCheckIn();
      cy.getByTestId('old-checkin-value').should('not.exist');
      CheckInDialog.do()
        .fillMetricCheckInValue('10')
        .setCheckInConfidence(0)
        .fillCheckInCommentary('changeinfo')
        .fillCheckInInitiatives('initiatives')
        .submit();
      cy.contains(`Letztes Check-in (${getCurrentDate()})`);
      keyresultDetailPage.createCheckIn();
      cy.contains('Letzter Wert').siblings('div').contains('10%');
    });
  });
});

function getCurrentDate() {
  const today = new Date();
  const yyyy = today.getFullYear();
  let mm = today.getMonth() + 1; // Months start at 0!
  let dd = today.getDate();

  let dd_str = '' + dd;
  let mm_str = '' + mm;
  if (dd < 10) dd_str = '0' + dd_str;
  if (mm < 10) mm_str = '0' + mm_str;

  return dd_str + '.' + mm_str + '.' + yyyy;
}

function checkForAttribute(title: string, value: string) {
  cy.get('mat-dialog-container').contains(value).parent().should('contain', title);
}
