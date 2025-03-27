import * as users from '../fixtures/users.json';
import CyOverviewPage from '../support/helper/dom-helper/pages/overviewPage';
import FilterHelper from '../support/helper/dom-helper/filterHelper';
import StatisticsPage from '../support/helper/dom-helper/pages/statisticsPage';
import MatSelectHelper from '../support/helper/dom-helper/MatSelectHelper';

describe('StatisticsPage', () => {
  let overviewPage = new CyOverviewPage();
  const statisticsPage = new StatisticsPage();

  beforeEach(() => {
    overviewPage = new CyOverviewPage();
    cy.loginAsUser(users.gl);
  });

  [[['Alle'],
    'GJ ForTests']].forEach(([teams,
    quarterID]: (string | string[])[]) => {
    it('Should route to statistics with the same param', () => {
      if (Array.isArray(teams)) {
        Array.from(teams)
          .forEach((team) => {
            FilterHelper.do()
              .toggleOption(team);
          });
      }
      if (typeof quarterID == 'string') {
        MatSelectHelper.do()
          .selectFromDropdown('app-quarter-filter', 'GJ ForTests');
      }


      cy.location('search')
        .then((overviewQuery) => {
          statisticsPage.visit();
          cy.location('search')
            .then((statisticsQuery) => {
              expect(statisticsQuery).to.eq(overviewQuery);
            });
        });
    });
  });

  it('Should display the statistics', () => {
    statisticsPage.visit();
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

  it('Should display statistics for quarter', () => {
    statisticsPage.visit();
    MatSelectHelper.do()
      .selectFromDropdown('app-quarter-filter', 'GJ ForTests');

    statisticsPage.validateKrsObjectives(0, 0, 0);
    statisticsPage.validateFinishedObjectives(0, '0/0', 0);
    statisticsPage.validateSuccessfulObjectives(0, '0/0', 0);
    statisticsPage.validateKrsMinTarget(0, '0/0', 0);
    statisticsPage.validateKrTypeRelation(
      0, '0%', 0, '0%', 0
    );
    statisticsPage.validateKrRelation(
      0, '0%', 0, '0%', 0, '0', 0, '0'
    );
  });
});
