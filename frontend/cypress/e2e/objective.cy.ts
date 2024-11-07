import * as users from '../fixtures/users.json';
import { onlyOn } from '@cypress/skip-test';
import { wait } from 'cypress-real-events/utils';

describe('OKR Objective e2e tests', () => {
  describe('tests via click', () => {
    describe('Functionality', () => {
      beforeEach(() => {
        cy.loginAsUser(users.gl);
        cy.visit('/?quarter=2');
      });

      it(`Release Objective from Draft to Ongoing`, () => {
        cy.getByTestId('add-objective').first().click();
        cy.fillOutObjective('A objective in state draft', 'safe-draft', undefined, '', false);

        cy.getByTestId('objective')
          .filter(':contains(A objective in state draft)')
          .last()
          .getByTestId('objective-state')
          .should('have.attr', 'src', `assets/icons/draft-icon.svg`);

        cy.getByTestId('objective')
          .filter(':contains(A objective in state draft)')
          .last()
          .getByTestId('three-dot-menu')
          .click()
          .wait(500)
          .get('.objective-menu-option')
          .contains('Objective veröffentlichen')
          .click();
        cy.contains('Objective veröffentlichen');
        cy.contains('Soll dieses Objective veröffentlicht werden?');
        cy.getByTestId('confirm-yes').click();
        cy.getByTestId('objective')
          .filter(':contains(A objective in state draft)')
          .last()
          .getByTestId('objective-state')
          .should('have.attr', 'src', `assets/icons/ongoing-icon.svg`);
      });

      it(`Complete Objective with Successful`, () => {
        cy.getByTestId('add-objective').first().click();
        cy.fillOutObjective('We want to complete this successful', 'safe', undefined, '', false);

        cy.getByTestId('objective')
          .filter(':contains("We want to complete this successful")')
          .last()
          .getByTestId('three-dot-menu')
          .click()
          .wait(500)
          .get('.objective-menu-option')
          .contains('Objective abschliessen')
          .click();

        cy.contains('Bewertung');
        cy.contains('Objective erreicht');
        cy.contains('Objective nicht erreicht');
        cy.contains('Kommentar (optional)');
        cy.contains('Objective abschliessen');
        cy.contains('Abbrechen');

        cy.getByTestId('successful').click();
        cy.getByTestId('submit').click();

        cy.getByTestId('objective')
          .filter(':contains("We want to complete this successful")')
          .last()
          .getByTestId('objective-state')
          .should('have.attr', 'src', `assets/icons/successful-icon.svg`);
      });
      it.only('delete objective with success', () => {
        cy.getByTestId('add-objective').first().click({ force: true });
        cy.fillOutObjective('We want to delete this objective', 'safe', undefined, '', false);
        cy.getByTestId('objective')
          .filter(':contains("We want to delete this objective")')
          .last()
          .getByTestId('three-dot-menu')
          .click({ force: true })
          .get('.objective-menu-option')
          .contains('Objective löschen')
          .click({ force: true });
        wait(200);
        cy.contains('Objective löschen');
        cy.contains(
          'Möchtest du dieses Objective wirklich löschen? Zugehörige Key Results werden dadurch ebenfalls gelöscht!',
        );
        cy.getByTestId('confirm-yes').click({ force: true });
        cy.get('We want to delete this objective').should('not.exist');
      });

      it(`Complete Objective with Not-Successful`, () => {
        cy.getByTestId('add-objective').first().click();
        cy.fillOutObjective('A not successful objective', 'safe', undefined, '', false);

        cy.getByTestId('objective')
          .filter(':contains("A not successful objective")')
          .last()
          .getByTestId('three-dot-menu')
          .click()
          .wait(500)
          .get('.objective-menu-option')
          .contains('Objective abschliessen')
          .click();

        cy.contains('Bewertung');
        cy.contains('Objective erreicht');
        cy.contains('Objective nicht erreicht');
        cy.contains('Kommentar (optional)');
        cy.contains('Objective abschliessen');
        cy.contains('Abbrechen');

        cy.getByTestId('not-successful').click();
        cy.getByTestId('submit').click();

        cy.getByTestId('objective')
          .filter(':contains("A not successful objective")')
          .last()
          .getByTestId('objective-state')
          .should('have.attr', 'src', `assets/icons/not-successful-icon.svg`);
      });

      it(`Reopen Successful Objective`, () => {
        cy.getByTestId('add-objective').first().click();
        cy.fillOutObjective('This objective will be reopened after', 'safe', undefined, '', false);

        cy.getByTestId('objective')
          .filter(':contains("This objective will be reopened after")')
          .last()
          .getByTestId('three-dot-menu')
          .click()
          .get('.objective-menu-option')
          .wait(500)
          .contains('Objective abschliessen')
          .click();

        cy.getByTestId('successful').click();
        cy.getByTestId('submit').click();

        cy.wait(500);

        cy.getByTestId('objective')
          .filter(':contains("This objective will be reopened after")')
          .last()
          .getByTestId('three-dot-menu')
          .click()
          .wait(500)
          .get('.objective-menu-option')
          .contains('Objective wiedereröffnen')
          .click();

        cy.contains('Objective wiedereröffnen');
        cy.contains('Soll dieses Objective wiedereröffnet werden?');
        cy.getByTestId('confirm-yes').click();

        cy.getByTestId('objective')
          .filter(':contains("This objective will be reopened after")')
          .last()
          .getByTestId('objective-state')
          .should('have.attr', 'src', `assets/icons/ongoing-icon.svg`);
      });

      it('Ongoing objective back to draft state', () => {
        onlyOn('chrome');
        cy.getByTestId('add-objective').first().click();
        cy.fillOutObjective('This objective will be returned to draft state', 'safe', undefined, '', false);

        cy.getByTestId('objective')
          .filter(':contains("This objective will be returned to draft state")')
          .last()
          .getByTestId('three-dot-menu')
          .click()
          .get('.objective-menu-option')
          .wait(500)
          .contains('Objective als Draft speichern')
          .click()
          .wait(500)
          .tabForward();
        cy.contains('Objective als Draft speichern');
        cy.contains('Soll dieses Objective als Draft gespeichert werden?');
        cy.getByTestId('confirm-yes').click();

        cy.getByTestId('objective')
          .filter(':contains("This objective will be returned to draft state")')
          .last()
          .getByTestId('objective-state')
          .should('have.attr', 'src', `assets/icons/draft-icon.svg`);
      });

      it(`Search for Objective`, () => {
        cy.getByTestId('add-objective').first().click();
        cy.fillOutObjective('Search after this objective', 'safe', undefined, '', false);

        cy.getByTestId('add-objective').first().click();
        cy.fillOutObjective('We dont want to search for this', 'safe', undefined, '', false);

        cy.contains('Search after this objective');
        cy.contains('We dont want to search for this');

        cy.scrollTo(0, 0);
        cy.wait(500);

        cy.getByTestId('objectiveSearch').first().click();
        cy.getByTestId('objectiveSearch').first().type('Search after').wait(350);

        cy.contains('Search after this objective');
        cy.get('We dont want to search for this').should('not.exist');

        cy.getByTestId('objectiveSearch').first().clear().type('this').wait(350);

        cy.contains('Search after this objective');
        cy.contains('We dont want to search for this');

        cy.getByTestId('objectiveSearch').first().clear().type('dont want to').wait(350);

        cy.contains('We dont want to search for this');
        cy.get('Search after this objective').should('not.exist');

        cy.getByTestId('objectiveSearch').first().clear().type('there is no objective').wait(350);

        cy.get('We dont want to search for this').should('not.exist');
        cy.get('Search after this objective').should('not.exist');
      });

      it(`Create Objective in other quarter`, () => {
        cy.getByTestId('add-objective').first().click();
        cy.fillOutObjective('Objective in quarter 2', 'safe', '2', '', false);

        cy.get('Objective in quarter 2').should('not.exist');

        cy.visit('/?quarter=2');

        cy.contains('Objective in quarter 2');
      });

      it(`Edit Objective and move to other quarter`, () => {
        cy.getByTestId('add-objective').first().click();
        cy.fillOutObjective('Move to another quarter on edit', 'safe', undefined, '', false);

        cy.getByTestId('objective')
          .filter(':contains("Move to another quarter on edit")')
          .last()
          .getByTestId('three-dot-menu')
          .click()
          .wait(500)
          .get('.objective-menu-option')
          .contains('Objective bearbeiten')
          .click();

        cy.fillOutObjective('Move to another quarter on edit', 'safe', '3', '', false);

        cy.get('Move to another quarter on edit').should('not.exist');

        cy.visit('/?quarter=3');

        cy.contains('Move to another quarter on edit');
      });
    });
  });
});

describe('tests via keyboard', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    cy.visit('/?quarter=2');
    onlyOn('chrome');
  });

  it(`Open objective aside via enter`, () => {
    cy.getByTestId('objective').first().find('[tabindex]').first().focus();
    cy.realPress('Enter');
    cy.url().should('include', 'objective');
  });
});
