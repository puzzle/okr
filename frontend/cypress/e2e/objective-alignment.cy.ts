import * as users from '../fixtures/users.json';

describe('OKR Objective Alignment e2e tests', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    cy.visit('/?quarter=2');
  });

  it(`Create Objective with an Alignment`, () => {
    cy.getByTestId('add-objective').first().click();

    cy.getByTestId('title').first().clear().type('Objective with new alignment');
    cy.get('select#alignment option:selected').should('contain.text', 'Bitte wählen');
    cy.get('select#alignment').select('K - Steigern der URS um 25%');
    cy.getByTestId('safe').click();

    cy.contains('Objective with new alignment');
    cy.getByTestId('objective')
      .filter(':contains("Objective with new alignment")')
      .last()
      .getByTestId('three-dot-menu')
      .click()
      .wait(500)
      .get('.objective-menu-option')
      .contains('Objective bearbeiten')
      .click();

    cy.get('select#alignment option:selected').should('contain.text', 'K - Steigern der URS um 25%');
  });

  it(`Update alignment of Objective`, () => {
    cy.getByTestId('add-objective').first().click();

    cy.getByTestId('title').first().clear().type('We change alignment of this Objective');
    cy.get('select#alignment option:selected').should('contain.text', 'Bitte wählen');
    cy.get('select#alignment').select('K - Steigern der URS um 25%');
    cy.getByTestId('safe').click();

    cy.contains('We change alignment of this Objective');
    cy.getByTestId('objective')
      .filter(':contains("We change alignment of this Objective")')
      .last()
      .getByTestId('three-dot-menu')
      .click()
      .wait(500)
      .get('.objective-menu-option')
      .contains('Objective bearbeiten')
      .click();

    cy.get('select#alignment').select('K - Antwortzeit für Supportanfragen um 33% verkürzen.');
    cy.getByTestId('safe').click();

    cy.getByTestId('objective')
      .filter(':contains("We change alignment of this Objective")')
      .last()
      .getByTestId('three-dot-menu')
      .click()
      .wait(500)
      .get('.objective-menu-option')
      .contains('Objective bearbeiten')
      .click();

    cy.get('select#alignment option:selected').should(
      'contain.text',
      'K - Antwortzeit für Supportanfragen um 33% verkürzen.',
    );
  });

  it(`Delete alignment of Objective`, () => {
    cy.getByTestId('add-objective').first().click();

    cy.getByTestId('title').first().clear().type('We delete the alignment');
    cy.get('select#alignment option:selected').should('contain.text', 'Bitte wählen');
    cy.get('select#alignment').select('K - Steigern der URS um 25%');
    cy.getByTestId('safe').click();

    cy.contains('We delete the alignment');
    cy.getByTestId('objective')
      .filter(':contains("We delete the alignment")')
      .last()
      .getByTestId('three-dot-menu')
      .click()
      .wait(500)
      .get('.objective-menu-option')
      .contains('Objective bearbeiten')
      .click();

    cy.get('select#alignment').select('Bitte wählen');
    cy.getByTestId('safe').click();

    cy.getByTestId('objective')
      .filter(':contains("We delete the alignment")')
      .last()
      .getByTestId('three-dot-menu')
      .click()
      .wait(500)
      .get('.objective-menu-option')
      .contains('Objective bearbeiten')
      .click();

    cy.get('select#alignment option:selected').should('contain.text', 'Bitte wählen');
  });

  it.only(`Alignment Possibilites change when quarter change`, () => {
    cy.visit('/?quarter=3');

    cy.getByTestId('add-objective').first().click();
    cy.getByTestId('title').first().clear().type('We can link later on this');
    cy.getByTestId('safe').click();

    cy.visit('/?quarter=2');

    cy.getByTestId('add-objective').first().click();
    cy.getByTestId('title').first().clear().type('Quarter change objective');
    cy.get('select#alignment option:selected').should('contain.text', 'Bitte wählen');
    cy.get('select#alignment').select(1);

    cy.get('select#alignment option:selected').should(
      'contain.text',
      'O - Wir wollen die Kundenzufriedenheit steigern',
    );

    cy.get('select#quarter').select('GJ 22/23-Q3');
    cy.getByTestId('title').first().clear().type('A new title');
    cy.get('select#alignment').select(1);
    cy.get('select#alignment option:selected').should('contain.text', 'O - We can link later on this');
  });
});
