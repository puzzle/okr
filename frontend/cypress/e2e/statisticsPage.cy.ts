import * as users from '../fixtures/users.json';
import CyOverviewPage from '../support/helper/dom-helper/pages/overviewPage';
import FilterHelper from '../support/helper/dom-helper/filterHelper';

describe('StatisticsPage', () => {
  let overviewPage = new CyOverviewPage();

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

      cy.location()
        .should((l) => {
          overviewPage.elements.statistics()
            .click();
          cy.location()
            .should((statisticsL) => {
              expect(statisticsL.search).to.eq(l.search);
            });
        });
    });
  });
});
