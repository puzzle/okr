import * as users from '../fixtures/users.json';
import { onlyOn } from '@cypress/skip-test';

describe('OKR Check-in e2e tests', () => {
  describe('tests via click', () => {
    beforeEach(() => {
      cy.loginAsUser(users.gl);
      cy.visit('/?quarter=2');
      onlyOn('chrome');
    });

    it(`Create checkin metric`, () => {
      cy.getByTestId('objective').first().getByTestId('add-keyResult').first().click();
      cy.getByTestId('submit').should('be.disabled');

      cy.fillOutKeyResult(
        'Very important keyresult',
        'PERCENT',
        '21',
        '52',
        null,
        null,
        null,
        null,
        'This is my description',
      );
      cy.getByTestId('submit').click();

      cy.getByTestId('keyresult').contains('Very important keyresult').click();

      cy.getByTestId('add-check-in').first().click();
      checkForDialogTextMetric();
      cy.fillOutCheckInMetric(30, true, 'We bought a new house', 'We have to buy more PCs');

      cy.contains('30%');
      cy.contains('6/10');
      cy.contains('Letztes Check-in (' + getCurrentDate() + ')');
      cy.contains('We bought a new house');
      cy.contains('Alle Check-ins anzeigen');
    });

    it(`Create checkin metric with value below baseline`, () => {
      cy.getByTestId('objective').first().getByTestId('add-keyResult').first().click();
      cy.getByTestId('submit').should('be.disabled');

      cy.fillOutKeyResult(
        'This will not be good',
        'PERCENT',
        '21',
        '52',
        null,
        null,
        null,
        null,
        'This is my description',
      );
      cy.getByTestId('submit').click();

      cy.getByTestId('keyresult').contains('This will not be good').click();

      cy.getByTestId('add-check-in').first().click();
      checkForDialogTextMetric();
      cy.fillOutCheckInMetric(5, false, null, null);

      cy.contains('5%');
      cy.contains('!');
      cy.contains('5/10');
      cy.contains('Letztes Check-in (' + getCurrentDate() + ')');
    });

    it('Create checkin ordinal', () => {
      cy.getByTestId('objective').first().getByTestId('add-keyResult').first().click();
      cy.getByTestId('submit').should('be.disabled');

      cy.getByTestId('titleInput').type('Title');
      cy.getByTestId('ordinalTab').click();

      cy.fillOutKeyResult(
        'A new ordinal keyresult for our company',
        null,
        null,
        null,
        'New house',
        'New car',
        'New pool',
        null,
        'This is my description',
      );
      cy.getByTestId('submit').click();

      cy.getByTestId('keyresult').contains('A new ordinal keyresult for our company').click();

      cy.getByTestId('add-check-in').first().click();
      checkForDialogTextOrdinal();
      cy.fillOutCheckInOrdinal(1, true, 'There is a new car', 'Buy now a new pool');

      cy.contains('6/10');
      cy.contains('There is a new car');
      cy.contains('Letztes Check-in (' + getCurrentDate() + ')');
    });

    it('Should generate checkin list', () => {
      cy.getByTestId('objective').first().getByTestId('add-keyResult').first().click();
      cy.getByTestId('submit').should('be.disabled');

      cy.fillOutKeyResult(
        'This will give a checkin list',
        'PERCENT',
        '21',
        '52',
        null,
        null,
        null,
        null,
        'This is my description',
      );
      cy.getByTestId('submit').click();

      cy.getByTestId('keyresult').contains('This will give a checkin list').click();

      cy.getByTestId('add-check-in').first().click();
      cy.fillOutCheckInMetric(30, false, 'We bought a new house', 'We have to buy more PCs');
      cy.wait(200);
      cy.getByTestId('add-check-in').first().click();
      cy.fillOutCheckInMetric(50, true, 'This was a good idea', 'Will be difficult');

      cy.getByTestId('show-all-checkins').click();

      cy.wait(500);
      cy.contains('Check-in History');
      cy.contains(getCurrentDate());
      cy.contains('Wert: 30%');
      cy.contains('Wert: 50%');
      cy.contains('Confidence: 5 / 10');
      cy.contains('Confidence: 6 / 10');
      cy.contains('Veränderungen: We bought a new house');
      cy.contains('Veränderungen: This was a good idea');
      cy.contains('Massnahmen: We have to buy more PCs');
      cy.contains('Massnahmen: Will be difficult');
      cy.contains('Schliessen');
    });

    it('Edit metric checkin', () => {
      cy.getByTestId('objective').first().getByTestId('add-keyResult').first().click();
      cy.getByTestId('submit').should('be.disabled');
      cy.fillOutKeyResult(
        'Here we edit a metric checkin',
        'CHF',
        '10',
        '300',
        null,
        null,
        null,
        null,
        'This is my description',
      );

      cy.getByTestId('submit').click();

      cy.getByTestId('keyresult').contains('Here we edit a metric checkin').click();

      cy.getByTestId('add-check-in').first().click();
      cy.fillOutCheckInMetric(30, false, 'Here we are', 'A cat would be great');
      cy.contains('Aktuell: CHF 30.-');
      cy.getByTestId('show-all-checkins').click();

      cy.wait(500);
      cy.contains('Check-in History');
      cy.getByTestId('edit-check-in').first().click();
      cy.contains('Here we edit a metric checkin');
      cy.contains('CHF 30.-');
      cy.contains('Confidence um Target Zone (CHF 213.-) zu erreichen');
      cy.contains('5/10');
      cy.getByTestId('check-in-metric-value').click().clear().type('200');
      cy.getByTestId('confidence-slider').realMouseDown();
      cy.contains('Here we are');
      cy.contains('A cat would be great');
      cy.getByTestId('changeInfo').clear().type('We bought a new sheep');
      cy.getByTestId('submit-check-in').click();

      cy.wait(200);
      cy.contains('CHF 200.-');
      cy.contains('We bought a new sheep');
    });

    it('Edit ordinal checkin', () => {
      cy.getByTestId('objective').first().getByTestId('add-keyResult').first().click();
      cy.getByTestId('submit').should('be.disabled');

      cy.getByTestId('titleInput').type('Title');
      cy.getByTestId('ordinalTab').click();

      cy.fillOutKeyResult(
        'For editing ordinal checkin',
        null,
        null,
        null,
        'New house',
        'New car',
        'New pool',
        null,
        'This is my description',
      );
      cy.getByTestId('submit').click();

      cy.getByTestId('keyresult').contains('For editing ordinal checkin').click();
      cy.getByTestId('add-check-in').first().click();
      cy.fillOutCheckInOrdinal(0, true, 'There is a new car', 'Buy now a new pool');
      cy.getByTestId('show-all-checkins').click();

      cy.wait(500);
      cy.contains('Check-in History');
      cy.getByTestId('edit-check-in').first().click();
      cy.contains('For editing ordinal checkin');
      cy.contains('Confidence um Target Zone zu erreichen');
      cy.contains('6/10');
      cy.getByTestId('stretch-radio').click();
      cy.getByTestId('confidence-slider').realMouseDown();
      cy.contains('There is a new car');
      cy.contains('Buy now a new pool');
      cy.getByTestId('changeInfo').clear().type('We bought a new dog');
      cy.getByTestId('submit-check-in').click();

      cy.wait(200);
      cy.contains('We bought a new dog');
      cy.contains('Buy now a new pool');
      cy.contains('STRETCH');
    });

    it(`Should display confirm dialog when creating checkin on draft objective`, () => {
      cy.getByTestId('add-objective').first().click();
      cy.fillOutObjective('draft objective title', 'safe-draft', '3');
      cy.visit('/?quarter=3');
      cy.contains('draft objective title').first().parentsUntil('#objective-column').last().focus();

      cy.tabForwardUntil('[data-testId="add-keyResult"]');
      cy.focused().contains('Key Result hinzufügen');
      cy.realPress('Enter');

      cy.fillOutKeyResult(
        'I am a metric keyresult for testing',
        'PERCENT',
        '21',
        '52',
        null,
        null,
        null,
        null,
        'This is my description',
      );
      cy.getByTestId('submit').click();

      cy.getByTestId('keyresult').contains('I am a metric keyresult for testing').click();
      cy.tabForward();
      cy.tabForward();
      cy.focused().contains('Check-in erfassen').click();
      cy.contains('Check-in im Draft-Status');
      cy.contains('Dein Objective befindet sich noch im DRAFT Status. Möchtest du das Check-in trotzdem erfassen?');
    });
  });
});

function checkForDialogTextMetric() {
  cy.contains('Very important keyresult');
  cy.contains('Check-in erfassen');
  cy.contains('Key Result');
  cy.contains('Neuer Wert');
  cy.contains('Confidence um Target Zone (42.7%) zu erreichen');
  cy.contains('Abbrechen');
}

function checkForDialogTextOrdinal() {
  cy.contains('A new ordinal keyresult for our company');
  cy.contains('Check-in erfassen');
  cy.contains('Key Result');
  cy.contains('Fail:');
  cy.contains('Commit / Target / Stretch noch nicht erreicht');
  cy.contains('Commit:');
  cy.contains('Target:');
  cy.contains('Stretch:');
  cy.contains('New car');
  cy.contains('New house');
  cy.contains('New pool');
  cy.contains('Confidence um Target Zone zu erreichen');
  cy.contains('Abbrechen');
}

function getCurrentDate() {
  const today = new Date();
  const yyyy = today.getFullYear();
  let mm = today.getMonth() + 1; // Months start at 0!
  let dd = today.getDate();

  if (dd < 10) dd = '0' + dd;
  if (mm < 10) mm = '0' + mm;

  return dd + '.' + mm + '.' + yyyy;
}
