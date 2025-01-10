import * as users from '../fixtures/users.json';
import FilterHelper from '../support/helper/dom-helper/filterHelper';

describe('okr overview', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  it('should have the current quarter with label "Aktuell"', () => {
    cy.getByTestId('quarter-filter')
      .contains('Aktuell');
  });

  it('should have correctly ordered teams', () => {
    FilterHelper.do()
      .optionShouldNotBeSelected('Alle')
      .toggleOption('Alle');
    const textsExpectedOrder = [
      'LoremIpsum',
      'Puzzle ITC',
      '/BBT',
      'we are cube.³'
    ];
    cy.get('.team-title:contains("we are cube.³")');
    cy.get('.team-title')
      .then((elements) => {
        const texts: string[] = elements.map((_, el) => Cypress.$(el)
          .text())
          .get();
        expect(texts).to.deep.equal(textsExpectedOrder);
      });
  });

  it('should use correct font', () => {
    cy.get('.team-title')
      .first()
      .invoke('css', 'font-family')
      .should('eq', 'Roboto, sans-serif');
    cy.get('.team-title')
      .first()
      .invoke('css', 'font-variation-settings')
      .should('eq', '"wght" 600');
  });
});
