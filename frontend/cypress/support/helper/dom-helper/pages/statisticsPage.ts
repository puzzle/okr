import { Page } from './page';

export default class StatisticsPage extends Page {
  private readonly contentClass = '.bg-white.h2.fw-bold.p-2';

  elements = {
    logo: () => cy.getByTestId('logo'),
    statistics: () => cy.getByTestId('statistics'),
    objectiveAmount: () => cy.getByTestId('objective-amount')
  };

  visitOverview(): void {
    this.elements.logo()
      .click();
  }

  getURL(): string {
    return '/statistics';
  }


  validatePage(): void {
    cy.contains('Auswertung')
      .should('exist');
    cy.contains('Objectives und Key Results')
      .should('exist');
    cy.contains('Anzahl abgeschlossener Objectives')
      .should('exist');
    cy.contains('Anzahl erreichter Objectives')
      .should('exist');
    cy.contains('Key Results mit min. Target Zone')
      .should('exist');
    cy.contains('Verhältnis metrische vs. ordinale Key Results')
      .should('exist');
    cy.contains('Verteilung der Zonen in den Key Results')
      .should('exist');
  }

  validateKrsObjectives(objective: number, keyResult: number, relation: number) {
    this.verifyStatisticsField('objectives-key-results', objective, 'Objectives');
    this.verifyStatisticsField('objectives-key-results', keyResult, 'KeyResults');
    this.verifyStatisticsField('objectives-key-results', relation, 'KR\'s/Objective');
  }

  validateFinishedObjectives(value: number, relation: string, barProgress: number) {
    this.verifyStatisticsField('completed-objectives', value);
    this.verifyStatisticsFieldSubTitle('completed-objectives', relation);
    this.verifyStatisticsCardBar('completed-objectives', barProgress);
  }

  validateSuccessfulObjectives(value: number, relation: string, barProgress: number) {
    this.verifyStatisticsField('successfully-completed-objectives', value);
    this.verifyStatisticsFieldSubTitle('successfully-completed-objectives', relation);
    this.verifyStatisticsCardBar('successfully-completed-objectives', barProgress);
  }

  validateKrsMinTarget(value: number, relation: string, barProgress: number) {
    this.verifyStatisticsField('kr-min-target', value);
    this.verifyStatisticsFieldSubTitle('kr-min-target', relation);
    this.verifyStatisticsCardBar('kr-min-target', barProgress);
  }

  validateKrTypeRelation(
    valueMetric: number, percentageMetric: string, valueOrdinal: number, percentageOrdinal: string, barProgress: number
  ) {
    this.verifyStatisticsField('kr-type-relation', valueMetric, 'metrisch');
    this.verifyStatisticsField('kr-type-relation', valueOrdinal, 'ordinal');
    this.verifyStatisticsFieldSubTitle('kr-type-relation', percentageMetric);
    this.verifyStatisticsFieldSubTitle('kr-type-relation', percentageOrdinal);
    this.verifyStatisticsCardBar('kr-type-relation', barProgress);
  }

  validateKrRelation(
    failAmount: number, percentageFail: string, commitAmount: number, percentageCommit: string, targetAmount: number, percentageTarget: string, stretchAmount: number, percentageStretch: string
  ) {
    this.verifyStatisticsField('kr-progress-relation', percentageFail);
    this.verifyStatisticsField('kr-progress-relation', percentageCommit);
    this.verifyStatisticsField('kr-progress-relation', percentageTarget);
    this.verifyStatisticsField('kr-progress-relation', percentageStretch);

    this.verifyStatisticsFieldSubTitle('kr-progress-relation', failAmount);
    this.verifyStatisticsFieldSubTitle('kr-progress-relation', commitAmount);
    this.verifyStatisticsFieldSubTitle('kr-progress-relation', targetAmount);
    this.verifyStatisticsFieldSubTitle('kr-progress-relation', stretchAmount);
  }

  protected doVisit(arg?: any): void {
    this.elements.statistics()
      .click();
  }

  private getStatisticsCard(id: string, title?: string): Cypress.Chainable {
    cy.getByTestId(id)
      .as('statistics-card');
    const statistics = cy.get('@statistics-card');
    if (title) {
      return statistics.contains('app-statistics-information', title);
    }
    return statistics;
  }

  private verifyStatisticsField(id: string, content: any, title?: string): void {
    this.getStatisticsCard(id, title)
      .find(this.contentClass)
      .should('include.text', content);
  }

  private verifyStatisticsFieldSubTitle(id: string, content: any, title?: string): void {
    this.getStatisticsCard(id, title)
      .should('include.text', content);
  }

  private verifyStatisticsCardBar(id: string, barProgress: any): void {
    this.getStatisticsCard(id)
      .find('mat-progress-bar')
      .should('have.attr', 'aria-valuenow')
      .should('match', new RegExp(`^${barProgress}`));
  }
}
