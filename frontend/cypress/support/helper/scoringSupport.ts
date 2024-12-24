import { isLastCheckInNegative } from '../../../src/app/shared/common';

interface ScoringValue {
  failPercent: number;
  commitPercent: number;
  targetPercent: number;
}

export function validateScoring(isOverview: boolean, percentage: number) {
  const rgbCode = colorFromPercentage(percentage);
  const scoringValue = scoringValueFromPercentage(percentage);

  if (percentage >= 100) {
    cy.getZone('stretch', isOverview)
      .should('have.attr', 'src')
      .should('include', 'star-filled-icon.svg');
  }

  validateScoringWidth('fail', scoringValue.failPercent, isOverview);
  validateScoringWidth('commit', scoringValue.commitPercent, isOverview);
  validateScoringWidth('target', scoringValue.targetPercent, isOverview);

  if (percentage == 0) {
    return;
  }
  validateScoringColor('fail', rgbCode, isOverview);
  validateScoringColor('commit', rgbCode, isOverview);
  validateScoringColor('target', rgbCode, isOverview);
}

export function getPercentageMetric(baseline: number, stretchGoal: number, value: number) {
  if (isLastCheckInNegative(baseline, stretchGoal, value)) {
    return -1;
  }
  return Math.abs(value - baseline) / Math.abs(stretchGoal - baseline) * 100;
}

export function getPercentageOrdinal(zone: string) {
  switch (zone) {
    case 'fail':
      return 30;
    case 'commit':
      return 70;
    case 'target':
      return 99.99;
    case 'stretch':
      return 101;
    default:
      return 0;
  }
}

function validateScoringWidth(zone: string, percent: number, isOverview: boolean) {
  cy.getZone(zone, isOverview)
    .parent()
    .invoke('width')
    .then((parentWidth) => {
      expect(parentWidth).not.to.equal(undefined);
      cy.getZone(zone, isOverview)
        .invoke('width')
        .should('be.within', parentWidth! * (percent / 100) - 3, parentWidth! * (percent / 100) + 3);
    });
}

function validateScoringColor(zone: string, rgbCode: string, isOverview: boolean) {
  cy.getZone(zone, isOverview)
    .invoke('css', 'background-color')
    .should('equal', rgbCode);
  if (rgbCode == 'rgba(0, 0, 0, 0)') {
    cy.getByTestId('star-scoring')
      .invoke('css', 'background-image')
      .should('contain', 'scoring-stars');
    checkVisibilityOfScoringComponent(isOverview, 'block', 'star-scoring');
    checkVisibilityOfScoringComponent(isOverview, 'none', 'normal-scoring');
  } else {
    checkVisibilityOfScoringComponent(isOverview, 'none', 'star-scoring');
    checkVisibilityOfScoringComponent(isOverview, 'flex', 'normal-scoring');
  }
}

function checkVisibilityOfScoringComponent(isOverview: boolean, displayProperty: string, componentTestId: string) {
  (isOverview ? cy.focused() : cy.getByTestId('side-panel'))
    .findByTestId(componentTestId)
    .invoke('css', 'display')
    .should('equal', displayProperty);
}

function colorFromPercentage(percentage: number) {
  switch (true) {
    case percentage >= 100:
      return 'rgba(0, 0, 0, 0)';
    case percentage > 70:
      return 'rgb(30, 138, 41)';
    case percentage > 30:
      return 'rgb(255, 214, 0)';
    default:
      return 'rgb(186, 56, 56)';
  }
}

function scoringValueFromPercentage(percentage: number): ScoringValue {
  switch (true) {
    case percentage >= 100:
      return {
        failPercent: 0,
        commitPercent: 0,
        targetPercent: 0
      };
    case percentage > 70:
      return {
        failPercent: 100,
        commitPercent: 100,
        targetPercent: (percentage - 70) * (100 / 30)
      };
    case percentage > 30:
      return {
        failPercent: 100,
        commitPercent: (percentage - 30) * (100 / 40),
        targetPercent: -1
      };
    default:
      return {
        failPercent: percentage * (100 / 30),
        commitPercent: -1,
        targetPercent: -1
      };
  }
}
