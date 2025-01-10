import { Page } from './page';
import TeamDialog from '../dialogs/teamDialog';
import AngularSearchBox from '../angularSearchBox';
import ConfirmDialog from '../dialogs/confirmDialog';

export default class TeamManagementPage extends Page {
  elements = {
    logo: () => cy.getByTestId('logo'),
    teamManagement: () => cy.getByTestId('team-management'),
    backToOverview: () => cy.getByTestId('routerLink-to-overview'),
    teamMenu: () => cy.get('app-team-list'),
    memberHeader: () => cy.get('#member-header'),
    registerMember: () => cy.getByTestId('invite-member'),
    addTeam: () => cy.getByTestId('add-team'),
    teamSearch: () => AngularSearchBox.from('app-team-management-banner [data-testId="teamManagementSearch"]')
  };

  override validatePage() {
    this.elements.teamManagement()
      .contains('Teamverwaltung');
    this.elements.backToOverview()
      .contains('Zurück zur OKR Übersicht');
    this.elements.addTeam()
      .contains('Team erfassen');
    this.elements.teamMenu()
      .contains('Alle Teams');
    this.elements.memberHeader()
      .contains('Alle Teams');
    this.elements.memberHeader()
      .contains('Members:');
    this.elements.registerMember()
      .contains('Member registrieren');
  }

  protected doVisit(): this {
    this.elements.logo()
      .click();
    return this;
  }

  backToOverview(): void {
    this.elements.backToOverview()
      .click();
  }

  visitOverview(): void {
    this.elements.logo()
      .click();
  }

  addTeam(): TeamDialog {
    this.elements.addTeam()
      .click();
    return new TeamDialog();
  }

  deleteTeam(teamName: string) {
    cy.get('app-team-list .mat-mdc-list-item')
      .contains(teamName)
      .click();
    cy.getByTestId('teamMoreButton')
      .click();
    cy.getByTestId('teamDeleteButton')
      .click();

    return ConfirmDialog.do()
      .checkForContentOnDialog('Team löschen')
      .checkForContentOnDialog(`Möchtest du das Team '${teamName}' wirklich löschen? Zugehörige Objectives werden dadurch in allen Quartalen ebenfalls gelöscht!`);
  }

  getURL(): string {
    return 'team-management';
  }
}
