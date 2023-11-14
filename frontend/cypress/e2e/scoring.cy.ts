import * as users from '../fixtures/users.json';
import { KeyResultMetricMin } from '../../src/app/shared/types/model/KeyResultMetricMin';
import { calculateCurrentPercentage, isInValid } from '../../src/app/shared/common';

describe('Scoring component e2e tests', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  [
    ['0', '100', '10'],
    ['0', '100', '31'],
    ['0', '100', '71'],
  ].forEach(([baseline, stretchgoal, value]) => {
    it.only('Create metric checkin and validate value of scoring component', () => {
      cy.createMetricKeyresult('Metric scoring keyresult', baseline, stretchgoal);
      cy.getByTestId('keyresult').get(':contains("Metric scoring keyresult")').last().click();
      cy.getByTestId('add-check-in').click();
      cy.getByTestId('key-result-metric-value').clear().type(value);
      cy.getByTestId('confidence-slider').click();
      cy.realPress('{rightarrow}').realPress('{rightarrow}').realPress('{rightarrow}');
      cy.getByTestId('continue-checkin').click();
      cy.getByTestId('changeInfo').click().type('Testveränderungen');
      cy.getByTestId('initiatives').click().type('Testmassnahmen');
      cy.getByTestId('create-checkin').click();
      validateScoringWidthsAndColor(false, +baseline, +stretchgoal, +value);
      cy.getByTestId('close-drawer').click();
      validateScoringWidthsAndColor(true, +baseline, +stretchgoal, +value);
    });
  });

  function validateScoringWidthsAndColor(isOverview: boolean, baseline: number, stretchGoal: number, value: number) {
    let percentage = -1;
    if (!isInValid(baseline, stretchGoal, value)) {
      percentage = (Math.abs(value - baseline) / Math.abs(stretchGoal - baseline)) * 100;
    }

    let rgbCode;

    switch (true) {
      case percentage > 70:
        rgbCode = 'rgb(30, 138, 41)';
        break;
      case percentage > 30:
        rgbCode = 'rgb(255, 214, 0)';
        break;
      default:
        rgbCode = 'rgb(186, 56, 56)';
        break;
    }

    let failPercent = -1;
    let commitPercent = -1;
    let targetPercent = -1;

    switch (true) {
      case percentage >= 100:
        failPercent = 100;
        commitPercent = 100;
        targetPercent = 101;
        break;
      case percentage > 70:
        failPercent = 100;
        commitPercent = 100;
        targetPercent = (100 / 30) * (percentage - 70);
        break;
      case percentage > 30:
        failPercent = 100;
        commitPercent = (100 / 40) * (percentage - 30);
        break;
      default:
        failPercent = (100 / 30) * percentage;
    }

    (isOverview ? cy.focused() : cy.getByTestId('side-panel'))
      .getByTestId('fail')
      .parent()
      .invoke('width')
      .then((width) => {
        if (width !== undefined) {
          (isOverview ? cy.focused() : cy.getByTestId('side-panel'))
            .getByTestId('fail')
            .invoke('width')
            .should('be.within', width * (failPercent / 100) - 2, width * (failPercent / 100) + 2);
        }
      });

    (isOverview ? cy.focused() : cy.getByTestId('side-panel'))
      .getByTestId('commit')
      .parent()
      .invoke('width')
      .then((width) => {
        if (width !== undefined) {
          (isOverview ? cy.focused() : cy.getByTestId('side-panel'))
            .getByTestId('commit')
            .invoke('width')
            .should('be.within', width * (commitPercent / 100) - 2, width * (commitPercent / 100) + 2);
        }
      });

    (isOverview ? cy.focused() : cy.getByTestId('side-panel'))
      .getByTestId('target')
      .parent()
      .invoke('width')
      .then((width) => {
        if (width !== undefined) {
          (isOverview ? cy.focused() : cy.getByTestId('side-panel'))
            .getByTestId('target')
            .invoke('width')
            .should('be.within', width * (targetPercent / 100) - 2, width * (targetPercent / 100) + 2);
        }
      });

    (isOverview ? cy.focused() : cy.getByTestId('side-panel'))
      .getByTestId('fail')
      .invoke('css', 'background-color')
      .should('equal', rgbCode);
    (isOverview ? cy.focused() : cy.getByTestId('side-panel'))
      .getByTestId('commit')
      .invoke('css', 'background-color')
      .should('equal', rgbCode);
    (isOverview ? cy.focused() : cy.getByTestId('side-panel'))
      .getByTestId('target')
      .invoke('css', 'background-color')
      .should('equal', rgbCode);
  }

  it('Create ordinal checkin and validate value of scoring component', () => {
    cy.createOrdinalKeyresult('Ordinal scoring keyresult', null);
    cy.getByTestId('keyresult').get(':contains("Ordinal scoring keyresult")').last().click();
    cy.getByTestId('add-check-in').click();
    cy.getByTestId('target-zone').click();
    cy.getByTestId('confidence-slider').click();
    cy.realPress('{rightarrow}').realPress('{rightarrow}').realPress('{rightarrow}');
    cy.getByTestId('continue-checkin').click();
    cy.getByTestId('changeInfo').click().type('Testveränderungen');
    cy.getByTestId('initiatives').click().type('Testmassnahmen');
    cy.getByTestId('create-checkin').click();
    //ToDo: Implement checking of scoring component value in keyresult detail
    cy.getByTestId('close-drawer').click();
    //ToDo: Implement checking of scoring component value on overview
  });
});
