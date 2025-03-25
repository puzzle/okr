import * as users from '../fixtures/users.json';
import CyOverviewPage from '../support/helper/dom-helper/pages/overviewPage';
import FilterHelper from '../support/helper/dom-helper/filterHelper';
import StatisticsPage from '../support/helper/dom-helper/pages/statisticsPage';

describe('StatisticsPage', () => {
  let overviewPage = new CyOverviewPage();
  const statisticsPage = new StatisticsPage();

  beforeEach(() => {
    overviewPage = new CyOverviewPage();
    cy.loginAsUser(users.gl);
  });

  [[['Alle'],
    1]].forEach(([teams,
    quarterID]: (number | string[])[]) => {
    it('Should route to statistics with the same param', () => {
      if (Array.isArray(teams)) {
        Array.from(teams)
          .forEach((team) => {
            FilterHelper.do()
              .toggleOption(team);
          });
      }
      if (typeof quarterID == 'number') {
        overviewPage.visitQuarter(quarterID);
      }

      cy.location('search')
        .then((overviewQuery) => {
          overviewPage.elements.statistics()
            .click();
          cy.location('search')
            .then((statisticsQuery) => {
              expect(statisticsQuery).to.eq(overviewQuery);
            });
        });
    });
  });

  it('Should display the statistics', () => {
    CyOverviewPage.do()
      .elements.statistics()
      .click();
    statisticsPage.validateKrsObjectives(5, 11, 2.2);
    statisticsPage.validateFinishedObjectives(0, '0/5', 0);
    statisticsPage.validateSuccessfulObjectives(0, '0/5', 0);
    statisticsPage.validateKrsMinTarget(27.3, '3/11', 27);
    statisticsPage.validateKrTypeRelation(
      9, '81.8%', 2, '18.2%', 81
    );
    statisticsPage.validateKrRelation(
      1, '9.1%', 7, '63.6%', 2, '18.2', 1, '9.1'
    );
  });
});
