import { Page } from './page';
import CyOverviewPage from './overviewPage';
import CheckInDialog from '../dialogs/checkInDialog';
import KeyResultDialog from '../dialogs/keyResultDialog';
import CheckInHistoryDialog from '../dialogs/checkInHistoryDialog';

export default class KeyResultDetailPage extends Page {
  elements = {
    logo: () => cy.getByTestId('logo'),
    closeDrawer: () => cy.getByTestId('close-drawer'),
    addCheckin: () => cy.getByTestId('add-check-in'),
    showAllCheckins: () => cy.getByTestId('show-all-checkins'),
    editKeyResult: () => cy.getByTestId('edit-keyResult')
  };

  override validatePage() {
    this.elements.addCheckin()
      .contains('Check-in erfassen');
    this.elements.editKeyResult()
      .contains('Key Result bearbeiten');
  }

  override visit(keyResultName: string): this {
    this.doVisit(keyResultName);
    return this.afterVisit();
  }

  protected doVisit(keyResultName: string): this {
    CyOverviewPage.do()
      .getKeyResultByName(keyResultName)
      .click();
    return this;
  }

  createCheckIn() {
    this.elements.addCheckin()
      .click();
    return new CheckInDialog();
  }

  editKeyResult() {
    this.elements.editKeyResult()
      .click();
    return new KeyResultDialog();
  }

  showAllCheckins() {
    this.elements.showAllCheckins()
      .click();
    return new CheckInHistoryDialog();
  }

  close(): void {
    this.elements.closeDrawer()
      .click({ force: true });
  }

  visitOverview(): void {
    this.elements.logo()
      .click();
  }

  getURL(): string {
    return '/details/keyresult';
  }
}
