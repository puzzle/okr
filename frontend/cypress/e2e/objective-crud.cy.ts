import * as users from '../fixtures/users.json';
import CyOverviewPage from '../support/helper/dom-helper/pages/overviewPage';
import ObjectiveDialog from '../support/helper/dom-helper/dialogs/objectiveDialog';

describe('crud operations', () => {
  let overviewPage = new CyOverviewPage();

  beforeEach(() => {
    overviewPage = new CyOverviewPage();
    cy.loginAsUser(users.gl);
  });

  [['ongoing objective title',
    'save',
    'ongoing-icon.svg'],
  ['draft objective title',
    'save-draft',
    'draft-icon.svg']].forEach(([objectiveTitle,
    buttonTestId,
    icon]) => {
    it('should create objective without key-results', () => {
      overviewPage.addObjective()
        .fillObjectiveTitle(objectiveTitle)
        .selectQuarter('3');
      cy.getByTestId(buttonTestId)
        .click();
      overviewPage.visitNextQuarter();
      overviewPage
        .getObjectiveByName(objectiveTitle)
        .findByTestId('objective-state')
        .should('have.attr', 'src', `assets/icons/${icon}`);
    });
  });

  it('should display error message when title not set', () => {
    overviewPage.addObjective();
    cy.getByTestId('title')
      .first()
      .type('title')
      .clear();
    cy.contains('Titel muss folgende Länge haben: 2-250 Zeichen');
    cy.getByTestId('save')
      .should('be.disabled');
    cy.getByTestId('save-draft')
      .should('be.disabled');
    cy.getByTestId('cancel')
      .should('not.be.disabled');
  });

  it('should cancel creating an objective', () => {
    const objectiveTitle = 'this is a canceled objective';
    overviewPage.addObjective()
      .selectQuarter('3')
      .cancel();
    overviewPage.visitNextQuarter();
    cy.contains(objectiveTitle)
      .should('not.exist');
  });

  it('should delete existing objective', () => {
    overviewPage.getFirstObjective()
      .findByTestId('three-dot-menu')
      .click();
    overviewPage.selectFromThreeDotMenu('Objective bearbeiten');
    ObjectiveDialog.do()
      .deleteObjective()
      .checkTitle('Objective löschen')
      .checkDescription('Möchtest du dieses Objective wirklich löschen? Zugehörige Key Results werden dadurch ebenfalls gelöscht!')
      .submit();
  });

  it('should open objective detail view via click', () => {
    overviewPage.getFirstObjective()
      .find('.title')
      .click();
    cy.url()
      .should('include', 'objective');
  });

  it('should edit objective', () => {
    const updatedTitle = 'This is an updated title';
    overviewPage.getFirstObjective()
      .findByTestId('three-dot-menu')
      .click();
    overviewPage.selectFromThreeDotMenu('Objective bearbeiten');
    ObjectiveDialog.do()
      .fillObjectiveTitle(updatedTitle)
      .submit();
    cy.contains(updatedTitle)
      .should('exist');
  });

  it('should duplicate objective', () => {
    const duplicatedTitle = 'This is a duplicated objective';
    overviewPage.getFirstObjective()
      .findByTestId('three-dot-menu')
      .click();
    overviewPage.selectFromThreeDotMenu('Objective duplizieren');
    ObjectiveDialog.do()
      .fillObjectiveTitle(duplicatedTitle)
      .submit();
    cy.contains(duplicatedTitle)
      .should('exist');
  });
});
