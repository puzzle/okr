import * as users from '../fixtures/users.json';
import CyOverviewPage from '../support/helper/pom-helper/pages/overviewPage';
import ObjectiveDialog from '../support/helper/pom-helper/dialogs/objectiveDialog';

describe('CRUD operations', () => {
  let op = new CyOverviewPage();

  beforeEach(() => {
    op = new CyOverviewPage();
    cy.loginAsUser(users.gl);
  });

  [
    ['ongoing objective title', 'save', 'ongoing-icon.svg'],
    ['draft objective title', 'save-draft', 'draft-icon.svg'],
  ].forEach(([objectiveTitle, buttonTestId, icon]) => {
    it(`Create objective, no keyresults`, () => {
      op.addObjective().fillObjectiveTitle(objectiveTitle).selectQuarter('3');
      cy.getByTestId(buttonTestId).click();
      cy.visit('/?quarter=3');
      op.getObjectiveByName(objectiveTitle)
        .findByTestId('objective-state')
        .should('have.attr', 'src', `assets/icons/${icon}`);
    });
  });

  it(`Create objective, should display error message`, () => {
    op.addObjective();
    cy.getByTestId('title').first().type('title').clear();
    cy.contains('Titel muss folgende Länge haben: 2-250 Zeichen');
    cy.getByTestId('save').should('be.disabled');
    cy.getByTestId('save-draft').should('be.disabled');
    cy.getByTestId('cancel').should('not.be.disabled');
  });

  it(`Create objective, cancel`, () => {
    const objectiveTitle = 'this is a canceled objective';
    op.addObjective().selectQuarter('3').cancel();
    cy.visit('/?quarter=3');
    cy.contains(objectiveTitle).should('not.exist');
  });

  it(`Delete existing objective`, () => {
    cy.get('.objective').first().findByTestId('three-dot-menu').click();
    op.selectFromThreeDotMenu('Objective bearbeiten');
    ObjectiveDialog.do()
      .deleteObjective()
      .checkTitle('Objective löschen')
      .checkDescription(
        'Möchtest du dieses Objective wirklich löschen? Zugehörige Key Results werden dadurch ebenfalls gelöscht!',
      )
      .submit();
  });

  it(`Open objective aside via click`, () => {
    cy.getByTestId('objective').first().find('.title').click();
    cy.url().should('include', 'objective');
  });

  it(`update objective`, () => {
    const updatedTitle = 'This is an updated title';
    cy.get('.objective').first().findByTestId('three-dot-menu').click();
    op.selectFromThreeDotMenu('Objective bearbeiten');
    ObjectiveDialog.do().fillObjectiveTitle(updatedTitle).submit();
    cy.contains(updatedTitle).should('exist');
  });

  it(`Duplicate objective`, () => {
    const duplicatedTitle = 'This is a duplicated objective';
    cy.get('.objective').first().findByTestId('three-dot-menu').click();
    op.selectFromThreeDotMenu('Objective duplizieren');
    ObjectiveDialog.do().fillObjectiveTitle(duplicatedTitle).submit();
    cy.contains(duplicatedTitle).should('exist');
  });
});
