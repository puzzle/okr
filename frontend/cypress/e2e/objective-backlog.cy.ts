import * as users from '../fixtures/users.json';

describe('OKR Objective Backlog e2e tests', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    cy.visit('/?quarter=2');
  });

  it(`Create Objective in backlog quarter should not have save button`, () => {
    cy.getByTestId('add-objective').first().click();

    cy.getByTestId('title').first().clear().type('Objective in quarter backlog');
    cy.get('select#quarter').select('Backlog');

    cy.contains('Speichern').should('not.exist');
    cy.contains('Als Draft speichern');
    cy.getByTestId('safe-draft').click();

    cy.get('Objective in quarter backlog').should('not.exist');

    cy.visit('/?quarter=199');

    cy.contains('Objective in quarter backlog');
  });

  it(`Edit Objective and move to backlog`, () => {
    cy.getByTestId('add-objective').first().click();
    cy.fillOutObjective('Move to another quarter on edit', 'safe-draft', undefined, '', false);

    cy.getByTestId('objective')
      .filter(':contains("Move to another quarter on edit")')
      .last()
      .getByTestId('three-dot-menu')
      .click()
      .wait(500)
      .get('.objective-menu-option')
      .contains('Objective bearbeiten')
      .click();

    cy.fillOutObjective('This goes now to backlog', 'safe', 'Backlog', '', false);

    cy.get('This goes now to backlog').should('not.exist');

    cy.visit('/?quarter=199');

    cy.contains('This goes now to backlog');
  });

  it(`Edit ongoing Objective can not choose backlog in quarter select`, () => {
    cy.getByTestId('add-objective').first().click();
    cy.fillOutObjective('We can not move this to backlog', 'safe', undefined, '', false);

    cy.getByTestId('objective')
      .filter(':contains("We can not move this to backlog")')
      .last()
      .getByTestId('three-dot-menu')
      .click()
      .wait(500)
      .get('.objective-menu-option')
      .contains('Objective bearbeiten')
      .click();

    cy.get('select#quarter').should('contain', 'GJ 23/24-Q1');
    cy.get('select#quarter').should('not.contain', 'Backlog');
  });

  it(`Can release Objective to another quarter from backlog`, () => {
    cy.visit('/?quarter=199');
    cy.getByTestId('add-objective').first().click();
    cy.getByTestId('title').first().clear().type('We can not release this');
    cy.getByTestId('safe').should('not.exist');
    cy.getByTestId('safe-draft').click();

    cy.getByTestId('objective')
      .filter(':contains("We can not release this")')
      .last()
      .getByTestId('three-dot-menu')
      .click();

    cy.wait(500);
    cy.contains('Objective bearbeiten');
    cy.contains('Objective duplizieren');
    cy.contains('Objective veröffentlichen');

    cy.get('.objective-menu-option').contains('Objective veröffentlichen').click();

    cy.contains('Objective veröffentlichen und in Quartal verschieben');

    cy.getByTestId('title').first().clear().type('This is our first released objective');

    cy.get('select#quarter').should('not.contain', 'Backlog');
    cy.get('select#quarter').select('GJ 22/23-Q4');

    cy.contains('Als Draft speichern').should('not.exist');
    cy.contains('Speichern');
    cy.getByTestId('safe').click();

    cy.contains('This is our first released objective').should('not.exist');

    cy.visit('/?quarter=1');

    cy.contains('This is our first released objective');
  });

  it(`Can edit Objective title in backlog`, () => {
    cy.visit('/?quarter=199');
    cy.getByTestId('add-objective').first().click();
    cy.fillOutObjective('This is possible for edit', 'safe-draft', undefined, '', false);

    cy.contains('This is possible for edit');

    cy.getByTestId('objective')
      .filter(':contains("This is possible for edit")')
      .last()
      .getByTestId('three-dot-menu')
      .click()
      .wait(500)
      .get('.objective-menu-option')
      .contains('Objective bearbeiten')
      .click();

    cy.fillOutObjective('My new title', 'safe', undefined, '', false);

    cy.contains('My new title');
  });

  it(`Can edit Objective in backlog and change quarter`, () => {
    cy.visit('/?quarter=199');
    cy.getByTestId('add-objective').first().click();
    cy.fillOutObjective('This goes to other quarter later', 'safe-draft', undefined, '', false);

    cy.getByTestId('objective')
      .filter(':contains("This goes to other quarter later")')
      .last()
      .getByTestId('three-dot-menu')
      .click()
      .wait(500)
      .get('.objective-menu-option')
      .contains('Objective bearbeiten')
      .click();

    cy.get('select#quarter').select('GJ 22/23-Q4');
    cy.getByTestId('safe').first().click();

    cy.visit('/?quarter=1');
    cy.contains('This goes to other quarter later');
  });

  it(`Can duplicate from backlog`, () => {
    cy.visit('/?quarter=199');
    cy.getByTestId('add-objective').first().click();
    cy.fillOutObjective('Ready for duplicate', 'safe-draft', undefined, '', false);

    cy.getByTestId('objective')
      .filter(':contains("Ready for duplicate")')
      .last()
      .getByTestId('three-dot-menu')
      .click()
      .wait(500)
      .get('.objective-menu-option')
      .contains('Objective duplizieren')
      .click();

    cy.fillOutObjective('This is a new duplication', 'safe', undefined, '', false);

    cy.contains('Ready for duplicate');
    cy.contains('This is a new duplication');

    cy.getByTestId('objective')
      .filter(':contains("Ready for duplicate")')
      .last()
      .getByTestId('three-dot-menu')
      .click()
      .wait(500)
      .get('.objective-menu-option')
      .contains('Objective duplizieren')
      .click();

    cy.fillOutObjective('New duplication for other quarter', 'safe', 'GJ 22/23-Q4', '', false);
    cy.get('New duplication for other quarter').should('not.exist');

    cy.visit('/?quarter=1');
    cy.contains('New duplication for other quarter');
  });

  it(`Can duplicate ongoing Objective to backlog`, () => {
    cy.getByTestId('add-objective').first().click();
    cy.fillOutObjective('Possible to duplicate into backlog', 'safe', undefined, '', false);

    cy.getByTestId('objective')
      .filter(':contains("Possible to duplicate into backlog")')
      .last()
      .getByTestId('three-dot-menu')
      .click()
      .wait(500)
      .get('.objective-menu-option')
      .contains('Objective duplizieren')
      .click();

    cy.get('select#quarter').select('Backlog');
    cy.getByTestId('safe').first().click();

    cy.visit('/?quarter=199');
    cy.contains('Possible to duplicate into backlog');
  });
});
