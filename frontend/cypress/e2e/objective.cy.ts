import * as users from '../fixtures/users.json';
import { onlyOn } from '@cypress/skip-test';

describe('OKR Objective e2e tests', () => {
  describe('tests via click', () => {
    describe('CRUD operations', () => {
      beforeEach(() => {
        cy.loginAsUser(users.gl);
        cy.visit('/?quarter=2');
      });

      [
        ['ongoing objective title', 'safe', 'ongoing-icon.svg'],
        ['draft objective title', 'safe-draft', 'draft-icon.svg'],
      ].forEach(([objectiveTitle, buttonTestId, icon]) => {
        it(`Create objective, no keyresults`, () => {
          cy.getByTestId('add-objective').first().click();
          cy.fillOutObjective(objectiveTitle, buttonTestId, '3');
          cy.visit('/?quarter=3');
          const objective = cy.contains(objectiveTitle).first().parentsUntil('#objective-column').last();
          objective.getByTestId('objective-state').should('have.attr', 'src', `assets/icons/${icon}`);
        });
      });

      [
        ['ongoing objective title', 'safe', 'ongoing-icon.svg'],
        ['draft objective title', 'safe-draft', 'draft-icon.svg'],
      ].forEach(([objectiveTitle, buttonTestId, icon]) => {
        it(`Create objective, no keyresults`, () => {
          cy.getByTestId('add-objective').first().click();
          cy.fillOutObjective(objectiveTitle, buttonTestId, '3', '', true);
          cy.contains('Key Result erfassen');
        });
      });

      it(`Create objective, should display error message`, () => {
        cy.getByTestId('add-objective').first().click();
        cy.getByTestId('description').first().clear().type('description');
        cy.contains('Dieses Feld muss ausgefüllt sein');
        cy.getByTestId('safe').should('be.disabled');
        cy.getByTestId('safe-draft').should('be.disabled');
        cy.getByTestId('cancel').should('not.be.disabled');
      });

      it(`Create objective, cancel`, () => {
        const objectiveTitle = 'this is a canceled objective';
        cy.getByTestId('add-objective').first().click();
        cy.fillOutObjective(objectiveTitle, 'cancel', '3');
        cy.visit('/?quarter=3');
        cy.contains(objectiveTitle).should('not.exist');
      });

      it(`Delete existing objective`, () => {
        cy.get('.objective').first().getByTestId('three-dot-menu').click();
        cy.get('.mat-mdc-menu-content').contains('Objective bearbeiten').click();
        cy.getByTestId('delete').click();
        cy.get("button[type='submit']").contains('Ja').click();
      });

      it(`Open objective aside via click`, () => {
        cy.getByTestId('objective').first().find('.title').click();
        cy.url().should('include', 'objective');
      });

      it(`update objective`, () => {
        const updatedTitle = 'This is an updated title';
        cy.get('.objective').first().getByTestId('three-dot-menu').click();
        cy.get('.mat-mdc-menu-content').contains('Objective bearbeiten').click();
        cy.fillOutObjective(updatedTitle, 'safe');
        cy.contains(updatedTitle).first();
      });

      it(`Duplicate objective`, () => {
        const duplicatedTitle = 'This is a duplicated objective';
        cy.get('.objective').first().getByTestId('three-dot-menu').click();
        cy.get('.mat-mdc-menu-content').contains('Objective duplizieren').click();
        cy.fillOutObjective(duplicatedTitle, 'safe');
        cy.contains(duplicatedTitle).first();
      });
    });

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
          .click();
        cy.get('.mat-mdc-menu-content').contains('Objective veröffentlichen').click();
        cy.getByTestId('confirmYes').click();

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
          .click();
        cy.get('.mat-mdc-menu-content').contains('Objective abschliessen').click();

        cy.contains('Bewertung');
        cy.contains('Objective erfolgreich');
        cy.contains('Objective nicht erfolgreich');
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

      it(`Complete Objective with Not-Successful`, () => {
        cy.getByTestId('add-objective').first().click();
        cy.fillOutObjective('A not successful objective', 'safe', undefined, '', false);

        cy.getByTestId('objective')
          .filter(':contains("A not successful objective")')
          .last()
          .getByTestId('three-dot-menu')
          .click();
        cy.get('.mat-mdc-menu-content').contains('Objective abschliessen').click();

        cy.contains('Bewertung');
        cy.contains('Objective erfolgreich');
        cy.contains('Objective nicht erfolgreich');
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
          .click();
        cy.get('.mat-mdc-menu-content').contains('Objective abschliessen').click();

        cy.getByTestId('successful').click();
        cy.getByTestId('submit').click();

        cy.wait(500);

        cy.getByTestId('objective')
          .filter(':contains("This objective will be reopened after")')
          .last()
          .getByTestId('three-dot-menu')
          .click();
        cy.get('.mat-mdc-menu-content').contains('Objective wiedereröffnen').click();

        cy.getByTestId('objective')
          .filter(':contains("This objective will be reopened after")')
          .last()
          .getByTestId('objective-state')
          .should('have.attr', 'src', `assets/icons/ongoing-icon.svg`);
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
        cy.getByTestId('objectiveSearch').first().type('Search after');

        cy.contains('Search after this objective');
        cy.get('We dont want to search for this').should('not.exist');

        cy.getByTestId('objectiveSearch').first().clear().type('this');

        cy.contains('Search after this objective');
        cy.contains('We dont want to search for this');

        cy.getByTestId('objectiveSearch').first().clear().type('dont want to');

        cy.contains('We dont want to search for this');
        cy.get('Search after this objective').should('not.exist');

        cy.getByTestId('objectiveSearch').first().clear().type('there is no objective');

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
          .click();
        cy.get('.mat-mdc-menu-content').contains('Objective bearbeiten').click();

        cy.fillOutObjective('Move to another quarter on edit', 'safe', '4', '', false);

        cy.get('Move to another quarter on edit').should('not.exist');

        cy.visit('/?quarter=4');

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
