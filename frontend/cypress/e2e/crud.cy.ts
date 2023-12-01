import * as users from '../fixtures/users.json';

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
    cy.getByTestId('title').first().type('description').clear();
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
