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
    cy.get('select#alignment')
      .contains('K - Steigern der URS um 25%')
      .then(($option) => {
        const optionValue = $option.attr('value');
        cy.get('select#alignment').select(optionValue!);
      });

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
    cy.get('select#alignment')
      .contains('K - Steigern der URS um 25%')
      .then(($option) => {
        const optionValue = $option.attr('value');
        cy.get('select#alignment').select(optionValue!);
      });
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

    cy.get('select#alignment')
      .contains('K - Antwortzeit für Supportanfragen um 33% verkürzen.')
      .then(($option) => {
        const optionValue = $option.attr('value');
        cy.get('select#alignment').select(optionValue!);
      });
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
    cy.get('select#alignment')
      .contains('K - Steigern der URS um 25%')
      .then(($option) => {
        const optionValue = $option.attr('value');
        cy.get('select#alignment').select(optionValue!);
      });
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

    cy.get('select#alignment').select('Kein Alignment');
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

  it(`Alignment Possibilites change when quarter change`, () => {
    cy.visit('/?quarter=1');

    cy.getByTestId('add-objective').first().click();
    cy.getByTestId('title').first().clear().type('We can link later on this');
    cy.getByTestId('safe').click();

    cy.getByTestId('add-objective').first().click();
    cy.getByTestId('title').first().clear().type('There is my other alignment');
    cy.get('select#alignment option:selected').should('contain.text', 'Bitte wählen');

    cy.get('select#alignment').select(1);

    cy.get('select#alignment option:selected').then(($select) => {
      const selectValue = $select.text();
      cy.getByTestId('quarterSelect').select('GJ 23/24-Q1');
      cy.getByTestId('title').first().clear().type('There is our other alignment');

      cy.get('select#alignment').select(1);

      cy.get('select#alignment option:selected').should('not.contain.text', selectValue);
      cy.getByTestId('cancel').click();

      cy.visit('/?quarter=2');

      cy.getByTestId('add-objective').first().click();
      cy.getByTestId('title').first().clear().type('Quarter change objective');

      cy.get('select#quarter').select('GJ 22/23-Q4');
      cy.getByTestId('title').first().clear().type('A new title');
      cy.get('select#alignment').select(1);

      cy.get('select#alignment option:selected').should('contain.text', selectValue);
    });
  });
});
