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
    cy.contains('Objectives')
      .parent()
      .find(this.contentClass)
      .should('include.text', objective);
    cy.contains('KeyResults')
      .parent()
      .find(this.contentClass)
      .should('include.text', keyResult);
    cy.contains('KR\'s/Objective')
      .parent()
      .find(this.contentClass)
      .should('include.text', relation);
  }

  validateFinishedObjectives(value: number, relation: string, barProgress: number) {
    cy.contains('Anzahl abgeschlossener Objectives')
      .parent()
      .find(this.contentClass)
      .should('include.text', value);
    cy.contains('Anzahl abgeschlossener Objectives')
      .parent()
      .should('include.text', relation);
    cy.contains('Anzahl abgeschlossener Objectives')
      .parent()
      .find('mat-progress-bar')
      .invoke('attr', 'ng-reflect-value')
      .should('match', new RegExp(`^${barProgress}`));
  }

  validateSuccessfulObjectives(value: number, relation: string, barProgress: number) {
    cy.contains('Anzahl erreichter Objectives')
      .parent()
      .find(this.contentClass)
      .should('include.text', value);
    cy.contains('Anzahl erreichter Objectives')
      .parent()
      .should('include.text', relation);
    cy.contains('Anzahl erreichter Objectives')
      .parent()
      .find('mat-progress-bar')
      .invoke('attr', 'ng-reflect-value')
      .should('match', new RegExp(`^${barProgress}`));
  }

  validateKrsMinTarget(value: number, relation: string, barProgress: number) {
    cy.contains('Key Results mit min. Target Zone')
      .parent()
      .find(this.contentClass)
      .should('include.text', value);
    cy.contains('Key Results mit min. Target Zone')
      .parent()
      .should('include.text', relation);
    cy.contains('Key Results mit min. Target Zone')
      .parent()
      .find('mat-progress-bar')
      .invoke('attr', 'ng-reflect-value')
      .should('match', new RegExp(`^${barProgress}`));
  }

  validateKrTypeRelation(
    valueMetric: number, percentageMetric: string, valueOrdinal: number, percentageOrdinal: string, barProgress: number
  ) {
    cy.contains('Verhältnis metrische vs. ordinale Key Results')
      .parent()
      .contains('metrisch')
      .parent()
      .find(this.contentClass)
      .should('include.text', valueMetric);
    cy.contains('Verhältnis metrische vs. ordinale Key Results')
      .parent()
      .contains('metrisch')
      .parent()
      .should('include.text', percentageMetric);

    cy.contains('Verhältnis metrische vs. ordinale Key Results')
      .parent()
      .contains('ordinal')
      .parent()
      .should('include.text', percentageOrdinal);
    cy.contains('Verhältnis metrische vs. ordinale Key Results')
      .parent()
      .contains('ordinal')
      .parent()
      .find(this.contentClass)
      .should('include.text', valueOrdinal);

    cy.contains('Verhältnis metrische vs. ordinale Key Results')
      .parent()
      .find('mat-progress-bar')
      .invoke('attr', 'ng-reflect-value')
      .should('match', new RegExp(`^${barProgress}`));
  }

  validateKrRelation(
    failAmount: number, percentageFail: string, commitAmount: number, percentageCommit: string, targetAmount: number, percentageTarget: string, stretchAmount: number, percentageStretch: string
  ) {
    cy.contains('Verteilung der Zonen in den Key Results')
      .parent()
      .contains('Fail')
      .parent()
      .find(this.contentClass)
      .should('include.text', percentageFail);
    cy.contains('Verteilung der Zonen in den Key Results')
      .parent()
      .contains('Fail')
      .parent()
      .should('include.text', failAmount);

    cy.contains('Verteilung der Zonen in den Key Results')
      .parent()
      .contains('Commit')
      .parent()
      .find(this.contentClass)
      .should('include.text', percentageCommit);
    cy.contains('Verteilung der Zonen in den Key Results')
      .parent()
      .contains('Commit')
      .parent()
      .should('include.text', commitAmount);

    cy.contains('Verteilung der Zonen in den Key Results')
      .parent()
      .contains('Target')
      .parent()
      .find(this.contentClass)
      .should('include.text', percentageTarget);
    cy.contains('Verteilung der Zonen in den Key Results')
      .parent()
      .contains('Target')
      .parent()
      .should('include.text', targetAmount);

    cy.contains('Verteilung der Zonen in den Key Results')
      .parent()
      .contains('Stretch')
      .parent()
      .find(this.contentClass)
      .should('include.text', percentageStretch);
    cy.contains('Verteilung der Zonen in den Key Results')
      .parent()
      .contains('Stretch')
      .parent()
      .should('include.text', stretchAmount);
  }

  protected doVisit(arg?: any): void {
    this.elements.statistics()
      .click();
  }
}
/*
 * Test ids: per card
 * Test ids: per card
 */
