import * as users from '../fixtures/users.json';

describe('OKR Objective Alignment e2e tests', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    cy.visit('/?quarter=2');
  });

  it(`Create Objective with an Alignment`, () => {
    cy.getByTestId('add-objective').first().click();

    cy.getByTestId('title').first().clear().type('Objective with new alignment');
    cy.getByTestId('alignmentInput').first().should('have.attr', 'placeholder', 'Bezug wählen');
    cy.tabForwardUntil('[data-testId="alignmentInput"]');
    cy.realPress('ArrowDown');
    cy.realPress('Enter');

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

    cy.getByTestId('alignmentInput')
      .first()
      .should('have.value', 'O - Als BBT wollen wir den Arbeitsalltag der Members von Puzzle ITC erleichtern.');
  });

  it(`Update alignment of Objective`, () => {
    cy.getByTestId('add-objective').first().click();

    cy.getByTestId('title').first().clear().type('We change alignment of this Objective');
    cy.tabForwardUntil('[data-testId="alignmentInput"]');
    cy.realPress('ArrowDown');
    cy.realPress('Enter');
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

    cy.tabForwardUntil('[data-testId="alignmentInput"]');
    cy.realPress('Delete');
    cy.realPress('ArrowDown');
    cy.realPress('ArrowDown');
    cy.realPress('Enter');
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

    cy.getByTestId('alignmentInput')
      .first()
      .should('have.value', 'KR - Das BBT hilft den Membern 20% mehr beim Töggelen');
  });

  it(`Delete alignment of Objective`, () => {
    cy.getByTestId('add-objective').first().click();

    cy.getByTestId('title').first().clear().type('We delete the alignment');
    cy.tabForwardUntil('[data-testId="alignmentInput"]');
    cy.realPress('ArrowDown');
    cy.realPress('Enter');
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

    cy.tabForwardUntil('[data-testId="alignmentInput"]');
    cy.realPress('Delete');
    cy.tabForward();
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

    cy.getByTestId('alignmentInput').first().should('have.attr', 'placeholder', 'Bezug wählen');
  });

  it(`Alignment Possibilities change when quarter change`, () => {
    cy.visit('/?quarter=1');

    cy.get('mat-chip:visible:contains("Alle")').click();
    cy.get('mat-chip:visible:contains("Alle")').click();
    cy.get('mat-chip:visible:contains("/BBT")').click();

    cy.getByTestId('add-objective').first().click();
    cy.getByTestId('title').first().clear().type('We can link later on this');
    cy.getByTestId('safe').click();

    cy.get('mat-chip:visible:contains("Alle")').click();

    cy.getByTestId('add-objective').first().click();
    cy.getByTestId('title').first().clear().type('There is my other alignment');
    cy.getByTestId('alignmentInput').first().should('have.attr', 'placeholder', 'Bezug wählen');
    cy.tabForwardUntil('[data-testId="alignmentInput"]');
    cy.realPress('ArrowDown');
    cy.realPress('Enter');

    cy.getByTestId('alignmentInput')
      .first()
      .invoke('val')
      .then((val) => {
        const selectValue = val;
        cy.getByTestId('quarterSelect').select('GJ 23/24-Q1');
        cy.getByTestId('title').first().clear().type('There is our other alignment');

        cy.tabForwardUntil('[data-testId="alignmentInput"]');
        cy.realPress('ArrowDown');
        cy.realPress('Enter');

        cy.getByTestId('alignmentInput').first().should('not.have.value', selectValue);

        cy.getByTestId('cancel').click();

        cy.visit('/?quarter=2');

        cy.getByTestId('add-objective').first().click();
        cy.getByTestId('title').first().clear().type('Quarter change objective');

        cy.get('select#quarter').select('GJ 22/23-Q4');
        cy.getByTestId('title').first().clear().type('A new title');
        cy.tabForwardUntil('[data-testId="alignmentInput"]');
        cy.realPress('ArrowDown');
        cy.realPress('Enter');

        cy.getByTestId('alignmentInput').first().should('have.value', selectValue);
      });
  });

  it(`Correct placeholder`, () => {
    cy.getByTestId('add-objective').first().click();
    cy.getByTestId('title').first().clear().type('This is an objective which we dont save');
    cy.getByTestId('alignmentInput').first().should('have.attr', 'placeholder', 'Bezug wählen');

    cy.getByTestId('quarterSelect').select('GJ 23/24-Q3');
    cy.getByTestId('title').first().clear().type('We changed the quarter');

    cy.getByTestId('alignmentInput').first().should('have.attr', 'placeholder', 'Kein Alignment vorhanden');
  });

  it(`Correct filtering`, () => {
    cy.getByTestId('add-objective').first().click();
    cy.getByTestId('title').first().clear().type('Die urs steigt');
    cy.getByTestId('safe').click();

    cy.scrollTo('top');
    cy.get('mat-chip:visible:contains("Puzzle ITC")').click();
    cy.get('mat-chip:visible:contains("/BBT")').click();
    cy.getByTestId('add-objective').first().click();
    cy.getByTestId('title').first().clear().type('Ein alignment objective');

    cy.getByTestId('alignmentInput').clear().type('urs');
    cy.realPress('ArrowDown');
    cy.realPress('Enter');

    cy.getByTestId('alignmentInput').first().should('have.value', 'O - Die urs steigt');

    cy.getByTestId('alignmentInput').clear().type('urs');
    cy.realPress('ArrowDown');
    cy.realPress('ArrowDown');
    cy.realPress('Enter');
    cy.getByTestId('alignmentInput').first().should('have.value', 'KR - Steigern der URS um 25%');
  });
});
