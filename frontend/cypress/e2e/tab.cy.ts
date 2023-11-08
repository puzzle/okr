import * as users from '../fixtures/users.json';
import { onlyOn } from '@cypress/skip-test';

describe('Tab workflow tests', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    onlyOn('chrome');
  });

  // it('First tabbable element is help element', () => {
  //   cy.tabForward();
  //   cy.focused().contains('Hilfe');
  // });
  //
  // it('Tab forward and then tab backwards with tab+shift', () => {
  //   cy.tabForward();
  //   cy.tabForward();
  //   cy.tabForward();
  //   cy.tabForward();
  //   cy.focused().contains('GJ');
  //   cy.tabBackward();
  //   cy.tabBackward();
  //   cy.tabBackward();
  //   cy.focused().contains('Hilfe');
  // });
  //
  // it('Open dialog via tab and enter', () => {
  //   cy.visit('/?quarter=2');
  //   cy.get('.objective').first().focus();
  //   cy.tabForwardUntil('[data-testId="add-keyResult"]');
  //   const button = cy.focused();
  //   button.click();
  //   cy.tabForward();
  //   cy.get('.mat-mdc-dialog-content').contains('Key Result erfassen');
  //   cy.tabBackward();
  //   cy.focused().click();
  //   cy.wait(500);
  //   button.then((buttonBefore) => {
  //     cy.focused().then((buttonAfter) => {
  //       expect(buttonBefore.get(0)).to.eql(buttonAfter.get(0));
  //     });
  //   });
  // });
  //
  // it('Tab to help element and user menu in banner', () => {
  //   cy.tabForward();
  //   cy.focused().contains('Hilfe');
  //   cy.tabForward(); cy.tabForward();
  //   cy.focused().contains('Jaya Norris');
  // });

  it('Tab to quarter filter, objective filter and teamfilter in filters', () => {
    cy.tabForward(); cy.tabForward(); cy.tabForward(); cy.tabForward();
    cy.focused().contains('GJ');
    cy.tabForward();
    cy.focused().type("Objective") // Decided to try writing since this is only possible in inputs. Sadly you can't use contains on placeholders otherwise I would have done that
    cy.tabForward(); cy.tabForward(); cy.tabForward();
    cy.focused().contains('Alle');
    cy.tabForward();
    cy.focused().contains('/BBT');
    cy.tabForward();
    cy.focused().contains('we are cube');
    cy.tabForward();
    cy.focused().contains('Puzzle ITC');
    cy.tabForward();
    cy.focused().contains('LoremIpsum');
  });

  // it('Tab to objective and open detail-dialog', () => {
  //   cy.tabForwardUntil('test id des Elementes hier einfügen')
  //   cy.url().should('include', 'objective');
  // });

  // it('Tab to key result and open detail-dialog', () => {
  //   cy.tabForwardUntil('test id des Elementes hier einfügen')
  //   cy.url().should('include', 'keyresult');
  // });

  // it('Tab to three dot menu and open possibilities', () => {
  //   cy.tabForwardUntil('test id des Elementes hier einfügen')
  // });

  it('Tab to add key result button and open dialog', () => {
    cy.tabForwardUntil('[data-testId="add-keyResult"]')
  });

//   it('Tab to add objective button and open dialog', () => {
//     cy.tabForwardUntil('test id des Elementes hier einfügen')
//   });
});
