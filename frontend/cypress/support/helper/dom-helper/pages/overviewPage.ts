import { filterByObjectiveName, filterByObjectiveState, getObjectiveColumns } from '../../objectiveHelper';
import ObjectiveDialog from '../dialogs/objectiveDialog';
import { Page } from './page';
import KeyResultDialog from '../dialogs/keyResultDialog';
import { filterByKeyResultName, getKeyResults } from '../../keyResultHelper';
import CompleteDialog from '../dialogs/completeDialog';

export default class CyOverviewPage extends Page {
  elements = {
    logo: () => cy.getByTestId('logo'),
    teamManagement: () => cy.getByTestId('team-management')
  };

  visitGJForTests() {
    this.visitQuarter(998);
  }

  visitBacklogQuarter() {
    this.visitQuarter(999);
  }

  visitCurrentQuarter() {
    this.visitQuarter(2);
  }

  visitNextQuarter() {
    this.visitQuarter(3);
  }

  private visitQuarter(quarter: number) {
    cy.visit(`/?quarter=${quarter}`);
  }

  addObjective(teamName?: string) {
    if (teamName) {
      this.getTeamByName(teamName)
        .findByTestId('add-objective')
        .first()
        .click();
      return new ObjectiveDialog();
    }
    cy.getByTestId('add-objective')
      .first()
      .click();
    return new ObjectiveDialog();
  }

  addKeyResult(teamName?: string, objectiveName?: string) {
    if (teamName && objectiveName) {
      this.getObjectiveByTeamAndName(teamName, objectiveName)
        .findByTestId('add-key-result')
        .first()
        .click();
    } else if (teamName) {
      this.getTeamByName(teamName)
        .findByTestId('add-key-result')
        .first()
        .click();
    } else if (objectiveName) {
      this.getObjectiveByName(objectiveName)
        .findByTestId('add-key-result')
        .first()
        .click();
    } else {
      cy.getByTestId('add-key-result')
        .first()
        .click();
    }

    return new KeyResultDialog();
  }

  addOngoingKeyResult() {
    this.getObjectiveByState('ongoing')
      .findByTestId('add-key-result')
      .first()
      .click();

    return new KeyResultDialog();
  }

  getTeamByName(teamName: string) {
    return cy.contains('.team-title', teamName)
      .parentsUntil('#overview')
      .last();
  }

  getFirstObjective() {
    return cy.get('.objective')
      .first();
  }

  getObjectiveByNameAndState(objectiveName: string, state: string) {
    this.getObjectivesByNameAndState(objectiveName, state)
      .last()
      .as('objective')
      .scrollIntoView();
    return cy.get('@objective');
  }

  getObjectivesByNameAndState(objectiveName: string, state: string) {
    return getObjectiveColumns()
      .filter(filterByObjectiveName(objectiveName))
      .filter(filterByObjectiveState(state));
  }

  getObjectiveByName(objectiveName: string) {
    this.getObjectivesByName(objectiveName)
      .last()
      .as('objective')
      .scrollIntoView();
    return cy.get('@objective');
  }

  getObjectiveByTeamAndName(teamName: string, objectiveName: string) {
    this.getTeamByName(teamName)
      .find('.objective')
      .filter(filterByObjectiveName(objectiveName))
      .last()
      .as('team')
      .scrollIntoView();

    return cy.get('@team');
  }

  getKeyResultOfObjective(objectiveName: string, keyResultName: string) {
    return this.getAllKeyResultsOfObjective(objectiveName)
      .filter(filterByKeyResultName(keyResultName));
  }

  getAllKeyResultsOfObjective(objectiveName: string) {
    return this.getObjectiveByName(objectiveName)
      .find('.key-result');
  }

  getObjectivesByName(objectiveName: string) {
    return getObjectiveColumns()
      .filter(filterByObjectiveName(objectiveName));
  }

  getObjectiveByState(state: string) {
    this.getObjectivesByState(state)
      .first()
      .as('objective')
      .scrollIntoView();
    return cy.get('@objective');
  }

  getObjectivesByState(state: string) {
    return getObjectiveColumns()
      .filter(filterByObjectiveState(state));
  }

  getKeyResultByName(keyResultName: string) {
    this.getKeyResultsByName(keyResultName)
      .last()
      .as('keyResult')
      .scrollIntoView();
    return cy.get('@keyResult');
  }

  getKeyResultsByName(keyResultName: string) {
    return getKeyResults()
      .filter(filterByKeyResultName(keyResultName));
  }

  selectFromThreeDotMenu(optionName: string) {
    cy.contains(optionName)
      .should('exist');
    cy.get('.objective-three-dot-menu')
      .contains(optionName)
      .as('option')
      .scrollIntoView();

    cy.get('@option')
      .should('have.class', 'objective-menu-option')
      .click();
  }

  duplicateObjective(objectiveName: string) {
    cy.intercept('GET', '**/objectives/*/keyResults')
      .as('keyResults');
    this.getObjectiveByName(objectiveName)
      .findByTestId('three-dot-menu')
      .as('objectiveThreeDotMenu')
      .scrollIntoView();
    cy.get('@objectiveThreeDotMenu')
      .should('exist')
      .should('be.visible')
      .click();
    this.selectFromThreeDotMenu('Objective duplizieren');
    cy.wait('@keyResults');
    return new ObjectiveDialog();
  }

  completeObjective(title: string) {
    this.addObjective()
      .fillObjectiveTitle(title)
      .submit();

    this
      .getObjectiveByNameAndState(title, 'ongoing')
      .findByTestId('three-dot-menu')
      .click();
    this.selectFromThreeDotMenu('Objective abschliessen');

    cy.contains('Bewertung');
    cy.contains('Objective erreicht');
    cy.contains('Objective nicht erreicht');
    cy.contains('Kommentar (optional)');
    cy.contains('Objective abschliessen');
    cy.contains('Abbrechen');

    return new CompleteDialog();
  }

  visitTeamManagement(): void {
    this.elements.teamManagement()
      .click();
  }

  getURL(): string {
    return '';
  }

  validatePage(): void {
    // Does not need to be implemented this comment is for making linter happy
  }

  protected doVisit(): void {
    this.elements.logo()
      .click();
  }
}
