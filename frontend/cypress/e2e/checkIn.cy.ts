import * as users from '../fixtures/users.json';

describe('OKR Check-in e2e tests', () => {
  describe('tests via click', () => {
    beforeEach(() => {
      cy.loginAsUser(users.gl);
      cy.visit('/?quarter=2');
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
      cy.fillOutCheckInMetric(30, 6, 'We bought a new house', 'We have to buy more PCs');

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
      cy.fillOutCheckInMetric(5, 5, null, null);

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
      cy.fillOutCheckInOrdinal(1, 4, 'There is a new car', 'Buy now a new pool');

      cy.contains('4/10');
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
      cy.fillOutCheckInMetric(30, 6, 'We bought a new house', 'We have to buy more PCs');
      cy.wait(200);
      cy.getByTestId('add-check-in').first().click();
      cy.fillOutCheckInMetric(50, 4, 'This was a good idea', 'Will be difficult');

      cy.getByTestId('show-all-checkins').click();

      cy.wait(500);
      cy.contains('Check-in History');
      cy.contains(getCurrentDate());
      cy.contains('Wert: 30%');
      cy.contains('Wert: 50%');
      cy.contains('Confidence: 6 / 10');
      cy.contains('Confidence: 4 / 10');
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
      cy.fillOutCheckInMetric(30, 5, 'Here we are', 'A cat would be great');
      cy.contains('Aktuell: CHF 30.-');
      cy.getByTestId('show-all-checkins').click();

      cy.wait(500);
      cy.contains('Check-in History');
      cy.getByTestId('edit-check-in').click();
      cy.contains('Here we edit a metric checkin');
      cy.contains('CHF 30.-');
      cy.contains('Confidence um Target (CHF 213.-) zu erreichen');
      cy.contains('5/10');
      cy.getByTestId('check-in-metric-value').clear().type('200');
      cy.getByTestId('confidence-slider').realMouseDown();
      cy.getByTestId('check-in-next').click();
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
      cy.fillOutCheckInOrdinal(0, 4, 'There is a new car', 'Buy now a new pool');
      cy.getByTestId('show-all-checkins').click();

      cy.wait(500);
      cy.contains('Check-in History');
      cy.getByTestId('edit-check-in').click();
      cy.contains('For editing ordinal checkin');
      cy.contains('Confidence um Target zu erreichen');
      cy.contains('3/10');
      cy.getByTestId('stretchZone').click();
      cy.getByTestId('confidence-slider').realMouseDown();
      cy.getByTestId('check-in-next').click();
      cy.contains('There is a new car');
      cy.contains('Buy now a new pool');
      cy.getByTestId('changeInfo').clear().type('We bought a new dog');
      cy.getByTestId('submit-check-in').click();

      cy.wait(200);
      cy.contains('We bought a new dog');
      cy.contains('Buy now a new pool');
      cy.contains('STRETCH');
    });
  });
});

function checkForDialogTextMetric() {
  cy.contains('Very important keyresult');
  cy.contains('Check-in erfassen (1/2)');
  cy.contains('Key Result');
  cy.contains('Aktueller Wert');
  cy.contains('Confidence um Target (42.7%) zu erreichen');
  cy.contains('Weiter');
  cy.contains('Abbrechen');
}

function checkForDialogTextOrdinal() {
  cy.contains('A new ordinal keyresult for our company');
  cy.contains('Check-in erfassen (1/2)');
  cy.contains('Key Result');
  cy.contains('Zone (Commit / Target / Stretch) noch nicht erreicht ');
  cy.contains('Commit:');
  cy.contains('Target:');
  cy.contains('Stretch:');
  cy.contains('New car');
  cy.contains('New house');
  cy.contains('New pool');
  cy.contains('Confidence um Target zu erreichen');
  cy.contains('Weiter');
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
