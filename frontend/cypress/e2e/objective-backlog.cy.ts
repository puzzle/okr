import * as users from '../fixtures/users.json';
import CyOverviewPage from '../support/helper/dom-helper/pages/overviewPage';
import ObjectiveDialog from '../support/helper/dom-helper/dialogs/objectiveDialog';

describe('okr objective backlog', () => {
  let overviewPage = new CyOverviewPage();

  beforeEach(() => {
    overviewPage = new CyOverviewPage();
    cy.loginAsUser(users.gl);
  });

  it(`should not have save button when creating objective in backlog quarter`, () => {
    overviewPage
      .addObjective()
      .fillObjectiveTitle('Objective in quarter backlog')
      .selectQuarter('Backlog')
      .run(cy.contains('Speichern').should('not.exist'))
      .run(cy.contains('Als Draft speichern'))
      .submitDraftObjective();

    cy.contains('Objective in quarter backlog').should('not.exist');

    overviewPage.visitBacklogQuarter();
    cy.contains('Objective in quarter backlog');
  });

  it(`should edit objective and move it to backlog`, () => {
    overviewPage.addObjective().fillObjectiveTitle('Move to another quarter on edit').submitDraftObjective();

    overviewPage
      .getObjectiveByNameAndState('Move to another quarter on edit', 'draft')
      .findByTestId('three-dot-menu')
      .click();
    overviewPage.selectFromThreeDotMenu('Objective bearbeiten');

    ObjectiveDialog.do().fillObjectiveTitle('This goes now to backlog').selectQuarter('Backlog').submit();

    cy.contains('This goes now to backlog').should('not.exist');

    overviewPage.visitBacklogQuarter();
    cy.contains('This goes now to backlog');
  });

  it(`should not be able to select backlog quarter when editing ongoing objective`, () => {
    overviewPage.addObjective().fillObjectiveTitle('We can not move this to backlog').submit();

    overviewPage
      .getObjectiveByNameAndState('We can not move this to backlog', 'ongoing')
      .findByTestId('three-dot-menu')
      .click();
    overviewPage.selectFromThreeDotMenu('Objective bearbeiten');

    cy.get('select#quarter').should('contain', 'GJ ForTests');
    cy.get('select#quarter').should('not.contain', 'Backlog');
  });

  it(`should release objective to another quarter from backlog`, () => {
    overviewPage.visitBacklogQuarter();
    overviewPage.addObjective().fillObjectiveTitle('We can not release this').submitDraftObjective();

    overviewPage.getObjectiveByNameAndState('We can not release this', 'draft').findByTestId('three-dot-menu').click();
    overviewPage.selectFromThreeDotMenu('Objective veröffentlichen');

    cy.contains('Objective veröffentlichen');
    cy.getByTestId('title').first().as('title');
    cy.get('@title').clear();
    cy.get('@title').type('This is our first released objective');

    cy.get('select#quarter').should('not.contain', 'Backlog');
    cy.get('select#quarter').select('GJ ForTests');

    cy.contains('Als Draft speichern').should('not.exist');
    cy.contains('Speichern');
    cy.getByTestId('save').click();

    cy.contains('This is our first released objective').should('not.exist');

    overviewPage.visitGJForTests();
    cy.contains('This is our first released objective');
  });

  it(`should edit objective title in backlog`, () => {
    overviewPage.visitBacklogQuarter();
    overviewPage.addObjective().fillObjectiveTitle('This is possible for edit').submitDraftObjective();

    overviewPage
      .getObjectiveByNameAndState('This is possible for edit', 'draft')
      .findByTestId('three-dot-menu')
      .click();
    overviewPage.selectFromThreeDotMenu('Objective bearbeiten');

    ObjectiveDialog.do().fillObjectiveTitle('My new title').submit();

    overviewPage.getObjectiveByNameAndState('My new title', 'draft');
  });

  it(`should edit objective in backlog and change quarter`, () => {
    overviewPage.visitBacklogQuarter();
    overviewPage.addObjective().fillObjectiveTitle('This goes to other quarter later').submitDraftObjective();

    overviewPage
      .getObjectiveByNameAndState('This goes to other quarter later', 'draft')
      .findByTestId('three-dot-menu')
      .click();
    overviewPage.selectFromThreeDotMenu('Objective bearbeiten');

    ObjectiveDialog.do().selectQuarter('GJ ForTests').submit();

    overviewPage.visitGJForTests();
    overviewPage.getObjectiveByNameAndState('This goes to other quarter later', 'draft');
  });

  it(`should duplicate objective in backlog`, () => {
    overviewPage.visitBacklogQuarter();
    overviewPage.addObjective().fillObjectiveTitle('Ready for duplicate in backlog').submitDraftObjective();

    overviewPage
      .getObjectiveByNameAndState('Ready for duplicate in backlog', 'draft')
      .findByTestId('three-dot-menu')
      .click();
    overviewPage.selectFromThreeDotMenu('Objective duplizieren');

    ObjectiveDialog.do().fillObjectiveTitle('This is a new duplication in backlog').submit();

    overviewPage.getObjectiveByNameAndState('Ready for duplicate in backlog', 'draft');
    overviewPage.getObjectiveByNameAndState('This is a new duplication in backlog', 'draft');
  });

  it('should duplicate objective from backlog to another quarter', () => {
    overviewPage.visitBacklogQuarter();
    overviewPage.addObjective().fillObjectiveTitle('Ready for duplicate to another quarter').submitDraftObjective();
    overviewPage
      .getObjectiveByNameAndState('Ready for duplicate to another quarter', 'draft')
      .findByTestId('three-dot-menu')
      .click();
    overviewPage.selectFromThreeDotMenu('Objective duplizieren');

    ObjectiveDialog.do().fillObjectiveTitle('New duplication from backlog').selectQuarter('GJ ForTests').submit();

    overviewPage.getObjectiveByNameAndState('Ready for duplicate to another quarter', 'draft').should('exist');
    cy.contains('New duplication from backlog').should('not.exist');
    overviewPage.visitGJForTests();
    overviewPage.getObjectiveByNameAndState('New duplication from backlog', 'draft').should('exist');
  });

  it(`should duplicate ongoing objective to backlog`, () => {
    overviewPage.addObjective().fillObjectiveTitle('Possible to duplicate into backlog').submit();

    overviewPage
      .getObjectiveByNameAndState('Possible to duplicate into backlog', 'ongoing')
      .findByTestId('three-dot-menu')
      .click();
    overviewPage.selectFromThreeDotMenu('Objective duplizieren');

    ObjectiveDialog.do().selectQuarter('Backlog').submit();

    overviewPage.visitBacklogQuarter();
    cy.contains('Possible to duplicate into backlog');
  });
});
