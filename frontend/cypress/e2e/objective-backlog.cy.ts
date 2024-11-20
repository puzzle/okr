import * as users from '../fixtures/users.json';
import CyOverviewPage from '../support/helper/pom-helper/pages/overviewPage';
import ObjectiveDialog from '../support/helper/pom-helper/dialogs/objectiveDialog';

describe('OKR Objective Backlog e2e tests', () => {
  let op = new CyOverviewPage();

  beforeEach(() => {
    op = new CyOverviewPage();
    cy.loginAsUser(users.gl);
  });

  it(`Create Objective in backlog quarter should not have save button`, () => {
    op.addObjective()
      .fillObjectiveTitle('Objective in quarter backlog')
      .selectQuarter('Backlog')
      .run(cy.contains('Speichern').should('not.exist'))
      .run(cy.contains('Als Draft speichern'))
      .submitDraftObjective();

    cy.contains('Objective in quarter backlog').should('not.exist');

    op.visitBacklogQuarter();
    cy.contains('Objective in quarter backlog');
  });

  it(`Edit Objective and move to backlog`, () => {
    op.addObjective().fillObjectiveTitle('Move to another quarter on edit').submitDraftObjective();

    op.getObjectiveByNameAndState('Move to another quarter on edit', 'draft').findByTestId('three-dot-menu').click();
    op.selectFromThreeDotMenu('Objective bearbeiten');

    ObjectiveDialog.do().fillObjectiveTitle('This goes now to backlog').selectQuarter('Backlog').submit();

    cy.contains('This goes now to backlog').should('not.exist');

    op.visitBacklogQuarter();
    cy.contains('This goes now to backlog');
  });

  it(`Edit ongoing Objective can not choose backlog in quarter select`, () => {
    op.addObjective().fillObjectiveTitle('We can not move this to backlog').submit();

    op.getObjectiveByNameAndState('We can not move this to backlog', 'ongoing').findByTestId('three-dot-menu').click();
    op.selectFromThreeDotMenu('Objective bearbeiten');

    cy.get('select#quarter').should('contain', 'GJ ForTests');
    cy.get('select#quarter').should('not.contain', 'Backlog');
  });

  it(`Can release Objective to another quarter from backlog`, () => {
    op.visitBacklogQuarter();
    op.addObjective().fillObjectiveTitle('We can not release this').submitDraftObjective();

    op.getObjectiveByNameAndState('We can not release this', 'draft').findByTestId('three-dot-menu').click();
    op.selectFromThreeDotMenu('Objective veröffentlichen');

    cy.contains('Objective veröffentlichen');
    cy.getByTestId('title').first().as('title');
    cy.get('@title').clear();
    cy.get('@title').type('This is our first released objective');

    cy.get('select#quarter').should('not.contain', 'Backlog');
    cy.get('select#quarter').select('GJ ForTests');

    cy.contains('Als Draft speichern').should('not.exist');
    cy.contains('Speichern');
    cy.getByTestId('safe').click();

    cy.contains('This is our first released objective').should('not.exist');

    op.visitGJForTests();
    cy.contains('This is our first released objective');
  });

  it(`Can edit Objective title in backlog`, () => {
    op.visitBacklogQuarter();
    op.addObjective().fillObjectiveTitle('This is possible for edit').submitDraftObjective();

    op.getObjectiveByNameAndState('This is possible for edit', 'draft').findByTestId('three-dot-menu').click();
    op.selectFromThreeDotMenu('Objective bearbeiten');

    ObjectiveDialog.do().fillObjectiveTitle('My new title').submit();

    op.getObjectiveByNameAndState('My new title', 'draft');
  });

  it(`Can edit Objective in backlog and change quarter`, () => {
    op.visitBacklogQuarter();
    op.addObjective().fillObjectiveTitle('This goes to other quarter later').submitDraftObjective();

    op.getObjectiveByNameAndState('This goes to other quarter later', 'draft').findByTestId('three-dot-menu').click();
    op.selectFromThreeDotMenu('Objective bearbeiten');

    ObjectiveDialog.do().selectQuarter('GJ ForTests').submit();

    op.visitGJForTests();
    op.getObjectiveByNameAndState('This goes to other quarter later', 'draft');
  });

  it(`Can duplicate in backlog`, () => {
    op.visitBacklogQuarter();
    op.addObjective().fillObjectiveTitle('Ready for duplicate in backlog').submitDraftObjective();

    op.getObjectiveByNameAndState('Ready for duplicate in backlog', 'draft').findByTestId('three-dot-menu').click();
    op.selectFromThreeDotMenu('Objective duplizieren');

    ObjectiveDialog.do().fillObjectiveTitle('This is a new duplication in backlog').submit();

    op.getObjectiveByNameAndState('Ready for duplicate in backlog', 'draft');
    op.getObjectiveByNameAndState('This is a new duplication in backlog', 'draft');
  });

  it('should duplicate from backlog', () => {
    op.visitBacklogQuarter();
    op.addObjective().fillObjectiveTitle('Ready for duplicate to another quarter').submitDraftObjective();
    op.getObjectiveByNameAndState('Ready for duplicate to another quarter', 'draft')
      .findByTestId('three-dot-menu')
      .click();
    op.selectFromThreeDotMenu('Objective duplizieren');

    ObjectiveDialog.do().fillObjectiveTitle('New duplication from backlog').selectQuarter('GJ ForTests').submit();

    op.getObjectiveByNameAndState('Ready for duplicate to another quarter', 'draft').should('exist');
    cy.contains('New duplication from backlog').should('not.exist');
    op.visitGJForTests();
    op.getObjectiveByNameAndState('New duplication from backlog', 'draft').should('exist');
  });

  it(`Can duplicate ongoing Objective to backlog`, () => {
    op.addObjective().fillObjectiveTitle('Possible to duplicate into backlog').submit();

    op.getObjectiveByNameAndState('Possible to duplicate into backlog', 'ongoing')
      .findByTestId('three-dot-menu')
      .click();
    op.selectFromThreeDotMenu('Objective duplizieren');

    ObjectiveDialog.do().selectQuarter('Backlog').submit();

    op.visitBacklogQuarter();
    cy.contains('Possible to duplicate into backlog');
  });
});
